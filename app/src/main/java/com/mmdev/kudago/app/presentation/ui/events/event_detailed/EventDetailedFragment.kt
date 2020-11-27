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

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentDetailedEventBinding
import com.mmdev.kudago.app.domain.events.data.EventDetailedInfo
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.ImagePagerAdapter
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.setHtmlText
import com.mmdev.kudago.app.presentation.ui.common.showToast
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class EventDetailedFragment : BaseFragment<FragmentDetailedEventBinding>(
	R.layout.fragment_detailed_event
), EventDetailedContract.View {


	override val presenter: EventDetailedPresenter by inject()

	private val eventsPhotosAdapter = ImagePagerAdapter()

	private val datesAdapter = DatesAdapter()


	private var receivedEventId = 0
	private companion object {
		private const val EVENT_ID_KEY = "EVENT_ID"
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			receivedEventId = it.getInt(EVENT_ID_KEY)
			presenter.loadEventDetailsById(receivedEventId)
		}

	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View = FragmentDetailedEventBinding.inflate(inflater, container, false).apply {
		_binding = this
	}.root

	override fun setupViews() {
		viewBinding.motionLayout.applySystemWindowInsets(applyTop = true)
		viewBinding.btnToolbarNavigation.setOnClickListener { navController.navigateUp() }
		
		viewBinding.vpPhotos.apply {
			adapter = eventsPhotosAdapter
		}

		TabLayoutMediator(viewBinding.tlDotsIndicator, viewBinding.vpPhotos){
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		viewBinding.rvDetailedDates.apply {
			adapter = datesAdapter
			layoutManager = GridLayoutManager(this.context, 1, RecyclerView.VERTICAL,false)

		}

		datesAdapter.setOnItemClickListener { view, position, item ->
			if (item.startInMillis > System.currentTimeMillis()) {
				val intent = setupCalendarIntent(
					item.startInMillis,
					item.endInMillis,
					datesAdapter.eventTitle
				)
				startActivity(intent)
			}
		}

		viewBinding.fabAddRemoveFavourites.setOnClickListener {
			presenter.addOrRemoveEventToFavourites()
		}
	}


	override fun updateData(data: EventDetailedInfo) {
		eventsPhotosAdapter.updateData(data.images.map { it.image })
		with(data.short_title.capitalizeRu()){
			viewBinding.tvToolbarTitle.text = this
			datesAdapter.eventTitle = this
		}

		viewBinding.tvDetailedAbout.setHtmlText(data.body_text)
		viewBinding.tvPrice.text = when {
			data.is_free -> getString(R.string.event_detailed_free)
			data.price.isNotBlank() -> data.price
			else -> getString(R.string.event_detailed_price_not_specified)
		}

	}

	override fun setEventDateTime(eventDates: List<EventDateUi>) {
		datesAdapter.updateData(eventDates)
	}

	override fun setRemoveTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_remove_text)
	}

	override fun setAddTextFab() {
		viewBinding.fabAddRemoveFavourites.text = getString(R.string.detailed_fab_add_text)
	}

	override fun showSuccessDeletedToast() = requireContext().showToast(getString(R.string.toast_successfully_removed_favourite))

	override fun showSuccessAddedToast() = requireContext().showToast(getString(R.string.toast_successfully_added_favourite))

	private fun setupCalendarIntent(startMillis: Long, endMillis: Long, eventTitle: String): Intent =
		Intent(Intent.ACTION_INSERT)
			.apply {
				data = CalendarContract.Events.CONTENT_URI
				putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
				if (endMillis != 0L) putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
				putExtra(CalendarContract.Events.TITLE, eventTitle)
				putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
				putExtra(CalendarContract.Events.STATUS, 1)
			}
	
	override fun onDestroyView() {
		viewBinding.rvDetailedDates.adapter = null
		viewBinding.vpPhotos.adapter = null
		super.onDestroyView()
	}
	
}