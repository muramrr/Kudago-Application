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

package com.mmdev.kudago.app.presentation.ui.events.categories

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentEventsCategoriesBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.events.categories.EventsCategoriesAdapter.EventsCategory

/**
 * This is the documentation block about the class
 */

class EventsCategoriesFragment : BaseFragment(R.layout.fragment_events_categories) {

	private val viewBinding by viewBinding(FragmentEventsCategoriesBinding::bind)

	private val mEventsCategoriesAdapter = EventsCategoriesAdapter()

	companion object {

		private const val CATEGORY_KEY = "CATEGORY"

	}


	override fun setupViews() {
		viewBinding.rvEventsCategories.applySystemWindowInsets(applyTop = true)
		viewBinding.rvEventsCategories.apply {
			adapter = mEventsCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
			addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
		}

		mEventsCategoriesAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<EventsCategory>{

			override fun onItemClick(item: EventsCategory, position: Int) {
				val category = bundleOf(CATEGORY_KEY to item.title)
				navController.navigate(R.id.action_eventsCategories_to_eventsCategoryDetailed,
				                             category)
			}
		})
	}

}