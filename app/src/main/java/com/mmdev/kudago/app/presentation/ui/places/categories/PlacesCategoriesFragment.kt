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

import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.places.category_detailed.PlacesContract
import com.mmdev.kudago.app.presentation.ui.places.category_detailed.PlacesPresenter
import kotlinx.android.synthetic.main.fragment_places_categories.*
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlacesCategoriesFragment : BaseFragment(R.layout.fragment_places_categories),
                                 PlacesContract.View {

	override val presenter: PlacesPresenter by inject()

	private val mPlacesCategoriesAdapter = PlacesCategoriesAdapter()



	override fun setupViews() {
		rvPlacesCategories.apply {
			adapter = mPlacesCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context)
		}
	}

	override fun updateData() {
		TODO("Not yet implemented")
	}

	override fun showLoading() {
		TODO("Not yet implemented")
	}

	override fun hideLoading() {
		TODO("Not yet implemented")
	}



}