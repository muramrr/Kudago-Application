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

package com.mmdev.kudago.app.presentation.ui.events.category_detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.core.utils.log.logDebug
import com.mmdev.kudago.app.core.utils.log.logInfo
import com.mmdev.kudago.app.databinding.FragmentCategoryDetailedBinding
import com.mmdev.kudago.app.domain.events.data.EventBaseInfo
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.custom.GridItemDecoration
import org.koin.android.ext.android.inject

/**
 * This is the documentation block about the class
 */

class EventsCategoryDetailedFragment: BaseFragment<FragmentCategoryDetailedBinding>(
	R.layout.fragment_category_detailed
), EventsContract.View {


	override val presenter: EventsPresenter by inject()

	private var receivedCategoryString = ""
	private var receivedTitleString = ""
	private val mAdapter = EventsCategoryDetailedAdapter().apply {
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

		private const val EVENT_ID_KEY = "EVENT_ID"
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
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View = FragmentCategoryDetailedBinding.inflate(inflater, container, false).apply {
		_binding = this
	}.root

	override fun setupViews() {
		viewBinding.toolbarCategoryTitle.apply {
			applySystemWindowInsets(applyTop = true)
			title = receivedTitleString
			setNavigationOnClickListener { navController.navigateUp() }
		}

		viewBinding.rvDetailedCategory.apply {
			setHasFixedSize(true)
			adapter = mAdapter
			layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
			addItemDecoration(GridItemDecoration())
			
			
		}

		mAdapter.setOnItemClickListener { view, position, item ->
			val eventId = bundleOf(EVENT_ID_KEY to item.id)
			navController.navigate(
				R.id.action_eventsCategoryDetailed_to_eventDetailed,
				eventId
			)
		}

	}
	
	override fun dataInit(data: List<EventBaseInfo>) {
		logInfo(TAG, "init data size = ${data.size}")
		mAdapter.setInitData(data)
	}
	
	override fun dataLoadedPrevious(data: List<EventBaseInfo>) {
		logInfo(TAG, "loaded previous size = ${data.size}")
		mAdapter.insertPreviousData(data)
	}
	
	override fun dataLoadedNext(data: List<EventBaseInfo>) {
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
		viewBinding.loading.root.visibility = View.VISIBLE
	}
	
	override fun hideLoading() {
		viewBinding.loading.root.visibility = View.INVISIBLE
	}
	
	override fun onDestroyView() {
		viewBinding.rvDetailedCategory.adapter = null
		super.onDestroyView()
	}
}