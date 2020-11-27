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

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mmdev.kudago.app.databinding.ItemCategoryDetailedBinding
import com.mmdev.kudago.app.domain.events.data.EventBaseInfo
import com.mmdev.kudago.app.presentation.base.BaseRecyclerAdapter
import com.mmdev.kudago.app.presentation.base.BaseViewHolder
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.utils.ImagePrefetcher

/**
 * This is the documentation block about the class
 */

class EventsCategoryDetailedAdapter(
	private var data: MutableList<EventBaseInfo> = mutableListOf()
): BaseRecyclerAdapter<EventBaseInfo>() {
	
	private companion object{
		private const val FIRST_POS = 0
		private const val OPTIMAL_ITEMS_COUNT = 40
	}
	private var startPos = 0
	private var itemsLoaded = 0
	
	private var scrollToTopListener: (() -> Unit)? = null
	private var scrollToBottomListener: (() -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		EventsCategoryDetailedViewHolder(
			ItemCategoryDetailedBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]
	
	fun setInitData(data: List<EventBaseInfo>) {
		this.data.clear()
		startPos = FIRST_POS
		this.data.addAll(data)
		itemsLoaded = data.size
		notifyDataSetChanged()
	}
	
	
	fun insertPreviousData(topData: List<EventBaseInfo>) {
		data.addAll(FIRST_POS, topData)
		notifyItemRangeInserted(FIRST_POS, topData.size)
		
		if (data.size > OPTIMAL_ITEMS_COUNT) {
			val shouldBeRemovedCount = data.size - OPTIMAL_ITEMS_COUNT
			data = data.dropLast(shouldBeRemovedCount).toMutableList()
			itemsLoaded -= shouldBeRemovedCount
			notifyItemRangeRemoved((data.size - 1), shouldBeRemovedCount)
		}
	}
	
	
	fun insertNextData(bottomData: List<EventBaseInfo>) {
		startPos = data.size
		data.addAll(bottomData)
		itemsLoaded += bottomData.size
		notifyItemRangeInserted(startPos, bottomData.size)
		if (data.size > OPTIMAL_ITEMS_COUNT) {
			val shouldBeRemovedCount = data.size - OPTIMAL_ITEMS_COUNT
			data = data.drop(shouldBeRemovedCount).toMutableList()
			notifyItemRangeRemoved(FIRST_POS, shouldBeRemovedCount)
		}
	}

	private fun prefetchData(data: List<EventBaseInfo>) {
		val prefetcher = ImagePrefetcher(data.map { it.images[0].image })
		prefetcher.prefetch()
	}
	
	fun setToTopScrollListener(listener: () -> Unit) {
		scrollToTopListener = listener
	}
	
	fun setToBottomScrollListener(listener: () -> Unit) {
		scrollToBottomListener = listener
	}
	
	
	
	inner class EventsCategoryDetailedViewHolder (private val viewBinding: ItemCategoryDetailedBinding):
			BaseViewHolder<EventBaseInfo>(viewBinding) {
		
		init {
			mClickListener?.let { listener ->
				itemView.setOnClickListener {
					listener.invoke(it, adapterPosition, getItem(adapterPosition))
				}
			}
		}

		override fun bind(item: EventBaseInfo) {
			if (adapterPosition > 10 && adapterPosition == (data.size - 6))
				scrollToBottomListener?.invoke()
			
			if (itemsLoaded > data.size && adapterPosition == 6)
				scrollToTopListener?.invoke()
			
			if (item.short_title.isNotBlank()) viewBinding.tvTitle.text = item.short_title.capitalizeRu()
			else viewBinding.tvTitle.text = item.title.capitalizeRu()
			//loading image from url
			viewBinding.ivImageHolder.load(item.images[0].image) {
				crossfade(true)
			}
		}


	}

}