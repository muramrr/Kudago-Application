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
import com.mmdev.kudago.app.databinding.ItemEventCategoryDetailedBinding
import com.mmdev.kudago.app.domain.events.EventEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.common.image_loader.ImageLoader

/**
 * This is the documentation block about the class
 */

class EventsCategoryDetailedAdapter(private var data: List<EventEntity> = emptyList()):
		BaseAdapter<EventEntity>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		EventsCategoryDetailedViewHolder(
				ItemEventCategoryDetailedBinding.inflate(LayoutInflater.from(parent.context),
				                                         parent,
				                                         false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]


	override fun setData(data: List<EventEntity>){
		this.data = data
		notifyDataSetChanged()
	}


	inner class EventsCategoryDetailedViewHolder (private val viewBinding:
	                                              ItemEventCategoryDetailedBinding):
			BaseViewHolder<EventEntity>(viewBinding.root) {


		override fun bind(item: EventEntity) {
			viewBinding.tvEventTitle.text = item.short_title
			//Picasso.get().load(item.images[0].image).into(viewBinding.ivImageHolder)
			ImageLoader.with(viewBinding.ivImageHolder.context)
				.load(viewBinding.ivImageHolder, item.images[0].image)
		}


	}

}