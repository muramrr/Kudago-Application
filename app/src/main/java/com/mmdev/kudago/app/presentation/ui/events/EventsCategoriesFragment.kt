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

package com.mmdev.kudago.app.presentation.ui.events

import androidx.core.os.bundleOf
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.base.CategoriesAdapter.AdapterCategoryItem
import com.mmdev.kudago.app.presentation.ui.base.CategoriesFragment

/**
 * This is the documentation block about the class
 */

class EventsCategoriesFragment : CategoriesFragment() {


	override fun setupAdapterList() = listOf(
			AdapterCategoryItem(title = getString(R.string.category_events_title_cinema),
			                    icon = R.drawable.ic_events_movies_24dp,
			                    apiIdentifier = getString(R.string.api_cinema)),
			AdapterCategoryItem(title = getString(R.string.category_events_title_theater),
			                    icon = R.drawable.ic_events_theater_24dp,
			                    apiIdentifier = getString(R.string.api_events_theater)),
			AdapterCategoryItem(title = getString(R.string.category_title_concert),
			                    icon = R.drawable.ic_events_concert_24dp,
			                    apiIdentifier =  getString(R.string.api_concert)),
			AdapterCategoryItem(title = getString(R.string.category_title_entertainment),
			                    icon = R.drawable.ic_events_entertainment_24dp,
			                    apiIdentifier = getString(R.string.api_entertainment)),
			AdapterCategoryItem(title = getString(R.string.category_title_exhibition),
			                    icon = R.drawable.ic_events_exhibition_24dp,
			                    apiIdentifier = getString(R.string.api_exhibition)),
			AdapterCategoryItem(title = getString(R.string.category_title_festival),
			                    icon = R.drawable.ic_events_festival_24dp,
			                    apiIdentifier = getString(R.string.api_festival)),
			AdapterCategoryItem(title = getString(R.string.category_title_party),
			                    icon = R.drawable.ic_events_party_24dp,
			                    apiIdentifier = getString(R.string.api_party)),
			AdapterCategoryItem(title = getString(R.string.category_title_fair),
			                    icon = R.drawable.ic_events_fair_24dp,
			                    apiIdentifier = getString(R.string.api_fair)),
			AdapterCategoryItem(title = getString(R.string.category_title_other),
			                    icon = R.drawable.ic_ic_events_other_24dp,
			                    apiIdentifier = getString(R.string.api_other)))

	override fun adapterItemClick(): BaseAdapter.OnItemClickListener<AdapterCategoryItem> =
		object : BaseAdapter.OnItemClickListener<AdapterCategoryItem>{

			override fun onItemClick(item: AdapterCategoryItem, position: Int) {
				val category = bundleOf(CATEGORY_KEY to item.apiIdentifier,
				                        TITLE_KEY to item.title)
				navController.navigate(R.id.action_eventsCategories_to_eventsCategoryDetailed,
				                       category)
			}
		}

}