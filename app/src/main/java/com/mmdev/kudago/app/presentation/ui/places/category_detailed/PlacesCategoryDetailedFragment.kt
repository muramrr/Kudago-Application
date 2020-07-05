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
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentCategoryDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.EndlessRecyclerViewScrollListener
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.custom.GridItemDecoration
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedFragment : BaseFragment(R.layout.fragment_category_detailed) ,
    PlacesContract.View {

	private val viewBinding by viewBinding(FragmentCategoryDetailedBinding::bind)

	override val presenter: PlacesPresenter by inject()

	private var receivedCategoryString = ""
	private var receivedTitleString = ""

	private val categoryDetailedAdapter = PlacesCategoryDetailedAdapter()

	companion object {

		private const val PLACE_ID_KEY = "PLACE_ID"
		private const val CATEGORY_KEY = "CATEGORY"
		private const val TITLE_KEY = "TITLE"

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			receivedCategoryString = it.getString(CATEGORY_KEY, "")
			receivedTitleString = it.getString(TITLE_KEY, "")
		}

		presenter.loadFirstCategoryEntities(receivedCategoryString)

	}

	override fun setupViews() {
		viewBinding.toolbarCategoryTitle.apply {
			applySystemWindowInsets(applyTop = true)
			title = receivedTitleString
			setNavigationOnClickListener { navController.navigateUp() }
		}

		val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
		viewBinding.rvDetailedCategory.apply {
			adapter = categoryDetailedAdapter
			layoutManager = gridLayoutManager
			addItemDecoration(GridItemDecoration())

			addOnScrollListener(object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
				override fun onLoadMore(page: Int, totalItemsCount: Int) {

					if (gridLayoutManager.findLastCompletelyVisibleItemPosition() <= totalItemsCount - 4) {
						presenter.loadMore()
					}

				}
			})
		}

		categoryDetailedAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<PlaceEntity> {

			override fun onItemClick(item: PlaceEntity, position: Int) {
				val placeId = bundleOf(PLACE_ID_KEY to item.id)
				navController.navigate(R.id.action_categoryDetailed_to_placeDetailed,
				                             placeId)
			}
		})

	}

	override fun updateData(data: List<PlaceEntity>) {
		categoryDetailedAdapter.setData(data)
		viewBinding.tvEmptyList.visibility = View.INVISIBLE
		viewBinding.ivEmptyList.visibility = View.INVISIBLE
	}

	override fun showEmptyList() {
		viewBinding.ivEmptyList.visibility = View.VISIBLE
		viewBinding.tvEmptyList.visibility = View.VISIBLE
	}


	override fun showLoading() {
	}

	override fun hideLoading() {
	}
}