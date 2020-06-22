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

package com.mmdev.kudago.app.presentation.ui.events.event_detailed

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentEventDetailedBinding
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
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

class EventDetailedFragment : BaseFragment(R.layout.fragment_event_detailed),
                              EventDetailedContract.View {

	private val viewBinding by viewBinding(FragmentEventDetailedBinding::bind)

	override val presenter: EventDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()


	private var receivedEventId = 0
	companion object {
		private const val EVENT_ID_KEY = "EVENT_ID"
	}


	@ExperimentalStdlibApi
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			receivedEventId = it.getInt(EVENT_ID_KEY)
			presenter.loadEventDetailsById(receivedEventId)
		}

	}

	override fun setupViews() {
		viewBinding.toolbarDetailed.applySystemWindowInsets(applyTop = true)
		viewBinding.tvToolbarTitle.applySystemWindowInsets(applyTop = true)

		viewBinding.toolbarNavigation.setOnClickListener { navController.navigateUp() }
		viewBinding.toolbarDetailed.setNavigationOnClickListener {  }

		viewBinding.vpPhotos.apply {
			adapter = placePhotosAdapter
		}

		TabLayoutMediator(viewBinding.tlDotsIndicator, viewBinding.vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		viewBinding.fabAddRemoveFavourites.setOnClickListener {
			presenter.addOrRemoveEventToFavourites()
		}
	}



	override fun showToast(toastText: String) = requireContext().showToast(toastText)

	override fun updateFabButton(fabText: String) {
		viewBinding.fabAddRemoveFavourites.text = fabText
	}


	@ExperimentalStdlibApi
	override fun updateData(data: EventDetailedEntity) {
		placePhotosAdapter.setData(data.images.map { it.image })
		viewBinding.tvToolbarTitle.text = data.short_title.capitalizeRu()
		viewBinding.tvDetailedDescription.text = data.body_text
	}


}