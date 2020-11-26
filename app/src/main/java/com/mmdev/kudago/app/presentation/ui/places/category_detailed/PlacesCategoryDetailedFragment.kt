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
import com.mmdev.kudago.app.core.utils.log.logDebug
import com.mmdev.kudago.app.core.utils.log.logInfo
import com.mmdev.kudago.app.databinding.FragmentCategoryDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.BaseRecyclerAdapter
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.custom.GridItemDecoration
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedFragment : BaseFragment(R.layout.fragment_category_detailed),
    PlacesContract.View {

	private val viewBinding by viewBinding(FragmentCategoryDetailedBinding::bind)

	override val presenter: PlacesPresenter by inject()

	private var receivedCategoryString = ""
	private var receivedTitleString = ""

	private val mAdapter = PlacesCategoryDetailedAdapter().apply {
		setToBottomScrollListener {
			logDebug(TAG, "invoked to load next page")
			presenter.loadNext()
		}
		setToTopScrollListener {
			logDebug(TAG, "invoked to load previous page")
			presenter.loadPrevious()
		}
	}

	private companion object {

		private const val PLACE_ID_KEY = "PLACE_ID"
		private const val CATEGORY_KEY = "CATEGORY"
		private const val TITLE_KEY = "TITLE"

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			receivedCategoryString = it.getString(CATEGORY_KEY, "")
			receivedTitleString = it.getString(TITLE_KEY, "")
			presenter.loadFirst(receivedCategoryString)
		}

	}

	override fun setupViews() {
		viewBinding.toolbarCategoryTitle.apply {
			applySystemWindowInsets(applyTop = true)
			title = receivedTitleString
			setNavigationOnClickListener { navController.navigateUp() }
		}

		viewBinding.rvDetailedCategory.apply {
			adapter = mAdapter
			layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
			addItemDecoration(GridItemDecoration())
			setHasFixedSize(true)
		}

		mAdapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<PlaceEntity> {

			override fun onItemClick(item: PlaceEntity, position: Int) {
				val placeId = bundleOf(PLACE_ID_KEY to item.id)
				navController.navigate(
					R.id.action_categoryDetailed_to_placeDetailed,
					placeId
				)
			}
		})

	}
	
	override fun dataInit(data: List<PlaceEntity>) {
		logInfo(TAG, "init data size = ${data.size}")
		mAdapter.setInitData(data)
	}
	
	override fun dataLoadedPrevious(data: List<PlaceEntity>) {
		logInfo(TAG, "loaded previous size = ${data.size}")
		mAdapter.insertPreviousData(data)
	}
	
	override fun dataLoadedNext(data: List<PlaceEntity>) {
		logInfo(TAG, "loaded next size = ${data.size}")
		mAdapter.insertNextData(data)
	}
	
	override fun hideEmptyListIndicator() {
		viewBinding.tvEmptyList.visibility = View.INVISIBLE
	}

	override fun showEmptyListIndicator() {
		viewBinding.tvEmptyList.visibility = View.VISIBLE
	}


	override fun showLoading() {
	}

	override fun hideLoading() {
	}
}