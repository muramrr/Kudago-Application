/*
 *
 * Copyright (c) 2020. Andrii Kovalchuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mmdev.kudago.app.presentation.ui.places.place_detailed

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentDetailedPlaceBinding
import com.mmdev.kudago.app.domain.places.data.PlaceCoords
import com.mmdev.kudago.app.domain.places.data.PlaceDetailedInfo
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.ImagePagerAdapter
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.setHtmlText
import com.mmdev.kudago.app.presentation.ui.common.showSnack
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlaceDetailedFragment: BaseFragment<FragmentDetailedPlaceBinding>(
	R.layout.fragment_detailed_place
), PlaceDetailedContract.View {

	override val presenter: PlaceDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()
	
	private val phoneNumbersAdapter = PhoneAdapter()

	private lateinit var mGoogleMap: GoogleMap


	private var receivedPlaceId = 0
	private companion object {
		private const val PLACE_ID_KEY = "PLACE_ID"
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		arguments?.let {
			receivedPlaceId = it.getInt(PLACE_ID_KEY)
			presenter.loadPlaceDetailsById(receivedPlaceId)
		}
		
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View = FragmentDetailedPlaceBinding.inflate(inflater, container, false).apply {
		_binding = this
	}.root

	override fun setupViews() {
		viewBinding.motionLayout.applySystemWindowInsets(applyTop = true)

		viewBinding.btnToolbarNavigation.setOnClickListener { navController.navigateUp() }

		viewBinding.vpPhotos.apply {
			adapter = placePhotosAdapter
		}

		TabLayoutMediator(viewBinding.tlDotsIndicator, viewBinding.vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		viewBinding.rvPhoneNumbers.apply {
			setHasFixedSize(true)
			adapter = phoneNumbersAdapter
			layoutManager = LinearLayoutManager(requireContext())
		}

		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync { googleMap -> mGoogleMap = googleMap }


		phoneNumbersAdapter.setOnItemClickListener { view, position, item ->
			buildDialog(item).show()
		}
		
		viewBinding.fabAddRemoveFavourites.setOnClickListener {
			presenter.addOrRemovePlaceToFavourites()
		}
	}

	override fun updateData(data: PlaceDetailedInfo) {
		placePhotosAdapter.updateData(data.images.map { it.image })
		viewBinding.tvToolbarTitle.text = data.short_title.capitalizeRu()
		viewBinding.tvDetailedAbout.setHtmlText(data.body_text)
		
		if (data.phone.isNotBlank()) {
			phoneNumbersAdapter.updateData(data.phone.split(", "))
		}
		else {
			phoneNumbersAdapter.updateData(
				listOf(getString(R.string.place_detailed_phone_is_not_specified))
			)
			viewBinding.rvPhoneNumbers.isEnabled = false
		}


	}

	override fun setMarkerOnMap(placeCoords: PlaceCoords, shortTitle: String) {
		val placeMarkerOnMap = LatLng(placeCoords.lat, placeCoords.lon)
		mGoogleMap.addMarker(MarkerOptions().position(placeMarkerOnMap).title(shortTitle))
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(placeMarkerOnMap))

	}

	override fun setRemoveTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_remove_text)
	}

	override fun setAddTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_add_text)
	}

	override fun showSuccessDeletedSnack() = viewBinding.root.showSnack(getString(R.string.successfully_removed_favourite))

	override fun showSuccessAddedSnack() = viewBinding.root.showSnack(getString(R.string.successfully_added_favourite))

	private fun buildDialog(phone: String): AlertDialog {
		
		val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.My_MaterialAlertDialog)
			.setItems(resources.getStringArray(R.array.place_detailed_phone_dialog_items)) { _, itemIndex ->
				when(itemIndex) {
					0 -> {
						val callIntent = Intent(Intent.ACTION_DIAL)
						callIntent.data = Uri.parse("tel:$phone")
						startActivity(callIntent)
					}
					1 -> {
						val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
						val clip = ClipData.newPlainText(phone, phone)
						clipboard.setPrimaryClip(clip)
						Snackbar.make(
							viewBinding.root,
							getString(R.string.place_detailed_phone_copy_formatter).format(phone),
							Snackbar.LENGTH_SHORT
						).show()
					}
					2 -> {
						//dismisses dialog
					}
				}
			}
			.create()
		
		val params = dialog.window?.attributes
		params?.gravity = Gravity.BOTTOM
		return dialog
	}
	
	
	override fun onDestroyView() {
		viewBinding.rvPhoneNumbers.adapter = null
		viewBinding.vpPhotos.adapter = null
		super.onDestroyView()
	}

}