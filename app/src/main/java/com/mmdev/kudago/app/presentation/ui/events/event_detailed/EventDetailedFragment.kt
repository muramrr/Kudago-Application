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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentDetailedEventBinding
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.UIEventDate
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.ImagePagerAdapter
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.setHtmlText
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class EventDetailedFragment : BaseFragment(R.layout.fragment_detailed_event),
                              EventDetailedContract.View {

	private val viewBinding by viewBinding(FragmentDetailedEventBinding::bind)

	override val presenter: EventDetailedPresenter by inject()

	private val placePhotosAdapter = ImagePagerAdapter()

	private val datesAdapter = EventDetailedDatesAdapter()


	private var receivedEventId = 0
	companion object {
		private const val EVENT_ID_KEY = "EVENT_ID"
	}


	@ExperimentalStdlibApi
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			receivedEventId = it.getInt(EVENT_ID_KEY)
			presenter.loadEventDetailsById(receivedEventId)
		}

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

		viewBinding.rvDetailedDates.apply {
			adapter = datesAdapter
			layoutManager = GridLayoutManager(this.context, 1, RecyclerView.VERTICAL,false)

		}

		viewBinding.fabAddRemoveFavourites.setOnClickListener {
			presenter.addOrRemoveEventToFavourites()
		}
	}


	@ExperimentalStdlibApi
	override fun updateData(data: EventDetailedEntity) {
		placePhotosAdapter.setData(data.images.map { it.image })
		viewBinding.tvToolbarTitle.text = data.short_title.capitalizeRu()
		viewBinding.tvDetailedAbout.setHtmlText(data.body_text)
		viewBinding.tvPrice.text = when {
			data.is_free -> getString(R.string.event_detailed_free)
			data.price.isNotBlank() -> data.price
			else -> getString(R.string.event_detailed_price_not_specified)
		}

	}

	override fun setEventDateTime(eventDates: List<UIEventDate>) {
		datesAdapter.setData(eventDates)
	}

	override fun setRemoveTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_remove_text)
	}

	override fun setAddTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_add_text)
	}

	override fun showSuccessDeletedToast() = showToast(getString(R.string.toast_successfully_removed_favourite))

	override fun showSuccessAddedToast() = showToast(getString(R.string.toast_successfully_added_favourite))


}