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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentPlacesCategoriesBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.places.categories.PlacesCategoriesAdapter.AdapterPlaceCategory

/**
 * This is the documentation block about the class
 */

class PlacesCategoriesFragment : BaseFragment(R.layout.fragment_places_categories) {

	private val viewBinding by viewBinding(FragmentPlacesCategoriesBinding::bind)

	private val mPlacesCategoriesAdapter = PlacesCategoriesAdapter()

	companion object {

		private const val CATEGORY_KEY = "CATEGORY"
		private const val TITLE_KEY = "TITLE"

	}


	override fun setupViews() {
		viewBinding.rvPlacesCategories.apply {
			applySystemWindowInsets(applyTop = true)
			adapter = mPlacesCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context)
			addItemDecoration(DividerItemDecoration(this.context, RecyclerView.VERTICAL))
		}

		mPlacesCategoriesAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<AdapterPlaceCategory>{

			override fun onItemClick(item: AdapterPlaceCategory, position: Int) {
				val category = bundleOf(CATEGORY_KEY to item.apiIdentifier,
				                        TITLE_KEY to item.title)
				navController.navigate(R.id.action_placesCategories_to_placesCategoryDetailed,
				                             category)
			}
		})
	}



}