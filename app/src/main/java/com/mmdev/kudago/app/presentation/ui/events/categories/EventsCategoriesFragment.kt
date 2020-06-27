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
import androidx.recyclerview.widget.RecyclerView
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentEventsCategoriesBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.events.categories.EventsCategoriesAdapter.AdapterEventsCategory

/**
 * This is the documentation block about the class
 */

class EventsCategoriesFragment : BaseFragment(R.layout.fragment_events_categories) {

	private val viewBinding by viewBinding(FragmentEventsCategoriesBinding::bind)

	companion object {

		private const val CATEGORY_KEY = "CATEGORY"
		private const val TITLE_KEY = "TITLE"

	}


	override fun setupViews() {
		viewBinding.rvEventsCategories.applySystemWindowInsets(applyTop = true)

		val mEventsCategoriesAdapter = EventsCategoriesAdapter(setupAdapterList())
		viewBinding.rvEventsCategories.apply {
			adapter = mEventsCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context)
			addItemDecoration(DividerItemDecoration(this.context, RecyclerView.VERTICAL))
		}

		mEventsCategoriesAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<AdapterEventsCategory>{

			override fun onItemClick(item: AdapterEventsCategory, position: Int) {
				val category = bundleOf(CATEGORY_KEY to item.apiIdentifier,
				                        TITLE_KEY to item.title)
				navController.navigate(R.id.action_eventsCategories_to_eventsCategoryDetailed,
				                             category)
			}
		})
	}

	private fun setupAdapterList() = listOf(
			AdapterEventsCategory(title = getString(R.string.category_events_title_cinema),
			                      picture = R.drawable.events_cinema,
			                      apiIdentifier = getString(R.string.api_cinema)),
			AdapterEventsCategory(title = getString(R.string.category_events_title_theater),
			                      picture = R.drawable.events_theater,
			                      apiIdentifier = getString(R.string.api_events_theater)),
			AdapterEventsCategory(title = getString(R.string.category_title_concert),
			                      picture = R.drawable.events_concert,
			                      apiIdentifier =  getString(R.string.api_concert)),
			AdapterEventsCategory(title = getString(R.string.category_title_entertainment),
			                      picture = R.drawable.events_entertainment,
			                      apiIdentifier = getString(R.string.api_entertainment)),
			AdapterEventsCategory(title = getString(R.string.category_title_exhibition),
			                      picture = R.drawable.events_exhibition,
			                      apiIdentifier = getString(R.string.api_exhibition)),
			AdapterEventsCategory(title = getString(R.string.category_title_festival),
			                      picture = R.drawable.events_festival,
			                      apiIdentifier = getString(R.string.api_festival)),
			AdapterEventsCategory(title = getString(R.string.category_title_party),
			                      picture = R.drawable.events_party,
			                      apiIdentifier = getString(R.string.api_party)),
			AdapterEventsCategory(title = getString(R.string.category_title_fair),
			                      picture = R.drawable.events_fair,
			                      apiIdentifier = getString(R.string.api_fair)),
			AdapterEventsCategory(title = getString(R.string.category_title_other),
			                      picture = R.drawable.events_other,
			                      apiIdentifier = getString(R.string.api_other)))

}