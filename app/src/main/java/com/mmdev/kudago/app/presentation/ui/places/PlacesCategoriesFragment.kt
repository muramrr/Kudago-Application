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

package com.mmdev.kudago.app.presentation.ui.places

import androidx.core.os.bundleOf
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.presentation.base.BaseRecyclerAdapter
import com.mmdev.kudago.app.presentation.ui.base.CategoriesAdapter.AdapterCategoryItem
import com.mmdev.kudago.app.presentation.ui.base.CategoriesFragment

/**
 * This is the documentation block about the class
 */

class PlacesCategoriesFragment : CategoriesFragment() {

	override fun setupAdapterList() = listOf(
		AdapterCategoryItem(
			title = getString(R.string.category_title_restaurants),
			icon = R.drawable.ic_places_restaurants_24dp,
			apiIdentifier = getString(R.string.api_restaurants)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_bar),
			icon = R.drawable.ic_places_bar_24dp,
			apiIdentifier = getString(R.string.api_bar)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_amusement),
			icon = R.drawable.ic_places_amusement_24dp,
			apiIdentifier = getString(R.string.api_amusement)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_anitcafe),
			icon = R.drawable.ic_places_anticafe_24dp,
			apiIdentifier = getString(R.string.api_anticafe)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_artcenter),
			icon = R.drawable.ic_places_artcenter_24dp,
			apiIdentifier = getString(R.string.api_artcenters)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_artspace),
			icon = R.drawable.ic_places_artspace_24dp,
			apiIdentifier = getString(R.string.api_artspace)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_places_title_cinema),
			icon = R.drawable.ic_places_cinema_24dp,
			apiIdentifier = getString(R.string.api_cinema)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_places_title_theatre),
			icon = R.drawable.ic_places_theater_24dp,
			apiIdentifier = getString(R.string.api_theatre)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_museums),
			icon = R.drawable.ic_places_museums_24dp,
			apiIdentifier = getString(R.string.api_museums)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_park),
			icon = R.drawable.ic_places_park_24dp,
			apiIdentifier = getString(R.string.api_park)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_clubs),
			icon = R.drawable.ic_places_clubs_24dp,
			apiIdentifier = getString(R.string.api_clubs)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_stripclub),
			icon = R.drawable.ic_places_stripclub_24dp,
			apiIdentifier = getString(R.string.api_stripclub)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_sights),
			icon = R.drawable.ic_places_sights_24dp,
			apiIdentifier = getString(R.string.api_sights)
		),
		AdapterCategoryItem(
			title = getString(R.string.category_title_other),
			icon = R.drawable.ic_places_other_24dp,
			apiIdentifier = getString(R.string.api_other)
		)
	)

	override fun adapterItemClick(): BaseRecyclerAdapter.OnItemClickListener<AdapterCategoryItem> =
		object : BaseRecyclerAdapter.OnItemClickListener<AdapterCategoryItem>{
			override fun onItemClick(item: AdapterCategoryItem, position: Int) {
				val category = bundleOf(
					CATEGORY_KEY to item.apiIdentifier,
					TITLE_KEY to item.title
				)
				navController.navigate(
					R.id.action_placesCategories_to_placesCategoryDetailed,
					category
				)
			}
		}
}