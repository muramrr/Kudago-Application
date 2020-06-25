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
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentEventsCategoryDetailedBinding
import com.mmdev.kudago.app.domain.events.EventEntity
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

class EventsCategoryDetailedFragment: BaseFragment(R.layout.fragment_events_category_detailed),
                                      EventsContract.View {

	private val viewBinding by viewBinding(FragmentEventsCategoryDetailedBinding::bind)

	override val presenter: EventsPresenter by inject()

	private var receivedCategoryString = ""
	private val categoryDetailedAdapter = EventsCategoryDetailedAdapter()

	companion object {

		private const val EVENT_ID_KEY = "EVENT_ID"
		private const val CATEGORY_KEY = "CATEGORY"

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			receivedCategoryString = it.getString(CATEGORY_KEY, "")
		}

		presenter.loadEvents(receivedCategoryString)

	}

	override fun setupViews() {
		viewBinding.toolbarCategoryTitle.apply {
			applySystemWindowInsets(applyTop = true)
			title = receivedCategoryString
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
						presenter.loadMoreEvents()
					}

				}
			})
		}

		categoryDetailedAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener<EventEntity> {

			override fun onItemClick(item: EventEntity, position: Int) {
				val eventId = bundleOf(EVENT_ID_KEY to item.id)
				navController.navigate(R.id.action_eventsCategoryDetailed_to_eventDetailed,
				                             eventId)
			}
		})

	}

	override fun updateData(data: List<EventEntity>) {
		categoryDetailedAdapter.setData(data)
	}

	override fun showLoading() {
		TODO("Not yet implemented")
	}

	override fun hideLoading() {
		TODO("Not yet implemented")
	}
}