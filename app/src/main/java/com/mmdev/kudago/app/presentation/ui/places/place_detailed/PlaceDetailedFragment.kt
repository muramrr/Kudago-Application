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

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentPlaceDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.ImagePagerAdapter
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.showToast
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlaceDetailedFragment: BaseFragment(R.layout.fragment_place_detailed),
                             PlaceDetailedContract.View {

	private val viewBinding by viewBinding(FragmentPlaceDetailedBinding::bind)

	override val presenter: PlaceDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()




	private var receivedPlaceId = 0
	companion object {
		private const val PLACE_ID_KEY = "PLACE_ID"
	}


	@ExperimentalStdlibApi
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			receivedPlaceId = it.getInt(PLACE_ID_KEY)
		}

		presenter.loadPlaceDetailsById(receivedPlaceId)

	}

	override fun setupViews() {
		viewBinding.toolbarDetailed.applySystemWindowInsets(applyTop = true)
		viewBinding.tvToolbarTitle.applySystemWindowInsets(applyTop = true)

		viewBinding.toolbarNavigation.setOnClickListener { navController.navigateUp() }

		viewBinding.vpPhotos.apply {
			adapter = placePhotosAdapter
		}

		TabLayoutMediator(viewBinding.tlDotsIndicator, viewBinding.vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		viewBinding.fabAddRemoveFavourites.setOnClickListener { presenter.addOrRemovePlaceToFavourites() }
	}

	override fun updateFabButton(fabText: String) {
		viewBinding.fabAddRemoveFavourites.text = fabText
	}

	@ExperimentalStdlibApi
	override fun updateData(data: PlaceDetailedEntity) {
		placePhotosAdapter.setData(data.images.map { it.image })
		viewBinding.tvToolbarTitle.text = data.short_title.capitalizeRu()
		viewBinding.tvDetailedDescription.text = data.body_text
	}

	override fun showToast(toastText: String) = requireContext().showToast(toastText)
}