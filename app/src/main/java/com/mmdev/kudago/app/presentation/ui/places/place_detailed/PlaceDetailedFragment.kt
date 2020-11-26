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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentDetailedPlaceBinding
import com.mmdev.kudago.app.domain.places.PlaceCoords
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.*
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlaceDetailedFragment: BaseFragment(R.layout.fragment_detailed_place),
                             PlaceDetailedContract.View {

	private val viewBinding by viewBinding(FragmentDetailedPlaceBinding::bind)

	override val presenter: PlaceDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()

	private lateinit var mGoogleMap: GoogleMap


	private var receivedPlaceId = 0
	private companion object {
		private const val PLACE_ID_KEY = "PLACE_ID"
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)


		arguments?.let {
			receivedPlaceId = it.getInt(PLACE_ID_KEY)
		}

		presenter.loadPlaceDetailsById(receivedPlaceId)

	}

	override fun setupViews() {
		viewBinding.motionLayout.applySystemWindowInsets(applyTop = true)

		viewBinding.btnToolbarNavigation.setOnClickListener { navController.navigateUp() }

		viewBinding.vpPhotos.apply {
			adapter = placePhotosAdapter
		}

		TabLayoutMediator(viewBinding.tlDotsIndicator, viewBinding.vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		viewBinding.btnPhoneNumber.apply {
			attachClickToCopyText(requireContext())
			setOnLongClickListener {
				buildDialog(viewBinding.btnPhoneNumber.text.toString()).show()
				return@setOnLongClickListener true
			}

		}

		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync { googleMap -> mGoogleMap = googleMap }


		viewBinding.fabAddRemoveFavourites.setOnClickListener {
			presenter.addOrRemovePlaceToFavourites()
		}
	}

	override fun updateData(data: PlaceDetailedEntity) {
		placePhotosAdapter.updateData(data.images.map { it.image })
		viewBinding.tvToolbarTitle.text = data.short_title.capitalizeRu()
		viewBinding.tvDetailedAbout.setHtmlText(data.body_text)
		if (data.phone.isNotBlank()) data.phone.replace(", ", "\n")
			.also { viewBinding.btnPhoneNumber.text = it }
		else {
			viewBinding.btnPhoneNumber.text = getString(R.string.place_detailed_phone_is_not_specified)
			viewBinding.btnPhoneNumber.isEnabled = false
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

	override fun showSuccessDeletedToast() = showToast(getString(R.string.toast_successfully_removed_favourite))

	override fun showSuccessAddedToast() = showToast(getString(R.string.toast_successfully_added_favourite))

	private fun buildDialog(phone: String): AlertDialog {
		val dialog = requireContext().showMaterialAlertDialogChooser(
			arrayOf("Call", "Copy", "Cancel"),
			listOf(
				{
			       val callIntent = Intent(Intent.ACTION_DIAL)
			       callIntent.data = Uri.parse("tel:$phone")
			       startActivity(callIntent)
				},
				{ viewBinding.btnPhoneNumber.performClick() },
				{ }
			)
		)
		val params = dialog.window?.attributes
		params?.gravity = Gravity.BOTTOM
		return dialog
	}



}