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

package com.mmdev.kudago.app.presentation.ui.places.category_detailed

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.EndlessRecyclerViewScrollListener
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.custom.GridItemDecoration
import kotlinx.android.synthetic.main.fragment_places_category_detailed.*
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedFragment : BaseFragment(R.layout.fragment_places_category_detailed) ,
    PlacesContract.View {


	override val presenter: PlacesPresenter by inject()

	private var receivedCategoryString = ""
	private val categoryDetailedAdapter = PlacesCategoryDetailedAdapter()

	companion object {

		private const val PLACE_ID_KEY = "PLACE_ID"
		private const val CATEGORY_KEY = "CATEGORY"

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			receivedCategoryString = it.getString(CATEGORY_KEY, "")
			presenter.loadPlaces(receivedCategoryString)
		}


	}

	override fun setupViews() {
		toolbarCategoryTitle.applySystemWindowInsets(applyTop = true)
		toolbarCategoryTitle.title = receivedCategoryString
		toolbarCategoryTitle.setNavigationOnClickListener { navController.navigateUp() }

		val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
		rvDetailedCategory.apply {
			adapter = categoryDetailedAdapter
			layoutManager = gridLayoutManager
			addItemDecoration(GridItemDecoration())

			addOnScrollListener(object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
				override fun onLoadMore(page: Int, totalItemsCount: Int) {

					if (gridLayoutManager.findLastCompletelyVisibleItemPosition() <= totalItemsCount - 4) {
						presenter.loadMorePlaces()
					}

				}
			})
		}

		categoryDetailedAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<PlaceEntity> {

			override fun onItemClick(item: PlaceEntity, position: Int) {
				val placeId = bundleOf(PLACE_ID_KEY to item.id)
				findNavController().navigate(R.id.action_categoryDetailed_to_placeDetailed,
				                             placeId)
			}
		})

	}

	override fun updateData(data: List<PlaceEntity>) {
		categoryDetailedAdapter.setData(data)
	}

	override fun showLoading() {
		TODO("Not yet implemented")
	}

	override fun hideLoading() {
		TODO("Not yet implemented")
	}
}