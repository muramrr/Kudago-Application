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
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.ImagePagerAdapter
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import kotlinx.android.synthetic.main.fragment_place_detailed.*
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlaceDetailedFragment: BaseFragment(R.layout.fragment_place_detailed),
	PlaceDetailedContract.View {

	override val presenter: PlaceDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()




	private var receivedPlaceId = 0
	companion object {
		private const val PLACE_ID_KEY = "PLACE_ID"
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			receivedPlaceId = it.getInt(PLACE_ID_KEY)
		}

		presenter.loadPlaceDetailsById(receivedPlaceId)

	}

	override fun setupViews() {
		toolbarPlaceDetailed.applySystemWindowInsets(applyTop = true)
		tvToolbarTitle.applySystemWindowInsets(applyTop = true)

		toolbarPlaceDetailed.setNavigationOnClickListener { navController.navigateUp() }

		vpPhotos.apply {
			adapter = placePhotosAdapter
		}

		TabLayoutMediator(tlDotsIndicator, vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()
	}

	override fun updateData(data: List<String>) {
		placePhotosAdapter.setData(data)
	}
}