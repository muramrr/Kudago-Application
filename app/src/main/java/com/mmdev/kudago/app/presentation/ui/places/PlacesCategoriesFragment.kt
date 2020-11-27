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

import android.view.View
import androidx.core.os.bundleOf
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.presentation.ui.categories.CategoriesFragment
import com.mmdev.kudago.app.presentation.ui.categories.CategoryData

/**
 * This is the documentation block about the class
 */

class PlacesCategoriesFragment : CategoriesFragment() {

	override fun setupAdapterList() = listOf(
		CategoryData(
			title = getString(R.string.category_title_restaurants),
			icon = R.drawable.ic_places_restaurants_24dp,
			apiIdentifier = getString(R.string.api_restaurants)
		),
		CategoryData(
			title = getString(R.string.category_title_bar),
			icon = R.drawable.ic_places_bar_24dp,
			apiIdentifier = getString(R.string.api_bar)
		),
		CategoryData(
			title = getString(R.string.category_title_amusement),
			icon = R.drawable.ic_places_amusement_24dp,
			apiIdentifier = getString(R.string.api_amusement)
		),
		CategoryData(
			title = getString(R.string.category_title_anitcafe),
			icon = R.drawable.ic_places_anticafe_24dp,
			apiIdentifier = getString(R.string.api_anticafe)
		),
		CategoryData(
			title = getString(R.string.category_title_artcenter),
			icon = R.drawable.ic_places_artcenter_24dp,
			apiIdentifier = getString(R.string.api_artcenters)
		),
		CategoryData(
			title = getString(R.string.category_title_artspace),
			icon = R.drawable.ic_places_artspace_24dp,
			apiIdentifier = getString(R.string.api_artspace)
		),
		CategoryData(
			title = getString(R.string.category_places_title_cinema),
			icon = R.drawable.ic_places_cinema_24dp,
			apiIdentifier = getString(R.string.api_cinema)
		),
		CategoryData(
			title = getString(R.string.category_places_title_theatre),
			icon = R.drawable.ic_places_theater_24dp,
			apiIdentifier = getString(R.string.api_theatre)
		),
		CategoryData(
			title = getString(R.string.category_title_museums),
			icon = R.drawable.ic_places_museums_24dp,
			apiIdentifier = getString(R.string.api_museums)
		),
		CategoryData(
			title = getString(R.string.category_title_park),
			icon = R.drawable.ic_places_park_24dp,
			apiIdentifier = getString(R.string.api_park)
		),
		CategoryData(
			title = getString(R.string.category_title_clubs),
			icon = R.drawable.ic_places_clubs_24dp,
			apiIdentifier = getString(R.string.api_clubs)
		),
		CategoryData(
			title = getString(R.string.category_title_stripclub),
			icon = R.drawable.ic_places_stripclub_24dp,
			apiIdentifier = getString(R.string.api_stripclub)
		),
		CategoryData(
			title = getString(R.string.category_title_sights),
			icon = R.drawable.ic_places_sights_24dp,
			apiIdentifier = getString(R.string.api_sights)
		),
		CategoryData(
			title = getString(R.string.category_title_other),
			icon = R.drawable.ic_places_other_24dp,
			apiIdentifier = getString(R.string.api_other)
		)
	)
	
	override fun adapterItemClick(view: View, position: Int, item: CategoryData) {
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