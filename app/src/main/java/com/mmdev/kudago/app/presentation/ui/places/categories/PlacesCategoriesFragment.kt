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

package com.mmdev.kudago.app.presentation.ui.places.categories

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentPlacesCategoriesBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets

/**
 * This is the documentation block about the class
 */

class PlacesCategoriesFragment : BaseFragment(R.layout.fragment_places_categories) {

	private val viewBinding by viewBinding(FragmentPlacesCategoriesBinding::bind)

	private val mPlacesCategoriesAdapter = PlacesCategoriesAdapter()

	companion object {

		private const val CATEGORY_KEY = "CATEGORY"

	}


	override fun setupViews() {
		viewBinding.rvPlacesCategories.applySystemWindowInsets(applyTop = true)
		viewBinding.rvPlacesCategories.apply {
			adapter = mPlacesCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context)
		}

		mPlacesCategoriesAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<String>{

			override fun onItemClick(item: String, position: Int) {
				val category = bundleOf(CATEGORY_KEY to item)
				navController.navigate(R.id.action_placesCategories_to_placesCategoryDetailed,
				                             category)
			}
		})
	}



}