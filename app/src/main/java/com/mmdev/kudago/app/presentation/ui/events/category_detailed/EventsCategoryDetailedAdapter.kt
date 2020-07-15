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
import com.mmdev.kudago.app.core.utils.image_loader.load
import com.mmdev.kudago.app.databinding.ItemCategoryDetailedBinding
import com.mmdev.kudago.app.domain.events.EventEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.utils.ImagePrefetcher

/**
 * This is the documentation block about the class
 */

class EventsCategoryDetailedAdapter(private val eventsList: MutableList<EventEntity> = mutableListOf()):
		BaseAdapter<EventEntity>() {

	private var startPos = 0

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		EventsCategoryDetailedViewHolder(
				ItemCategoryDetailedBinding.inflate(LayoutInflater.from(parent.context),
				                                         parent,
				                                         false)
		)

	override fun getItemCount(): Int = eventsList.size
	override fun getItem(position: Int) = eventsList[position]

	override fun setData(data: List<EventEntity>){
		eventsList.addAll(data)
		notifyItemRangeInserted(startPos, data.size)
	}

	fun updateData(data: List<EventEntity>) {
		startPos = eventsList.size
		prefetchData(data)
		eventsList.addAll(data)
		notifyItemRangeInserted(startPos, data.size)
	}

	private fun prefetchData(data: List<EventEntity>) {
		val prefetcher = ImagePrefetcher(data.map { it.images[0].image })
		prefetcher.prefetch()
	}


	inner class EventsCategoryDetailedViewHolder (private val viewBinding: ItemCategoryDetailedBinding):
			BaseViewHolder<EventEntity>(viewBinding.root) {


		@ExperimentalStdlibApi
		override fun bind(item: EventEntity) {
			if (item.short_title.isNotBlank()) viewBinding.tvTitle.text = item.short_title.capitalizeRu()
			else viewBinding.tvTitle.text = item.title.capitalizeRu()
			//loading image from url
			viewBinding.ivImageHolder.load(item.images[0].image)
		}


	}

}