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

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.ItemCategoryDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import com.mmdev.kudago.app.presentation.ui.common.image_loader.ImageLoader

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedAdapter (private var data: List<PlaceEntity> = emptyList()):
		BaseAdapter<PlaceEntity>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		PlacesCategoryDetailedViewHolder(
				ItemCategoryDetailedBinding.inflate(LayoutInflater.from(parent.context),
				                                    parent,
				                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]


	override fun setData(data: List<PlaceEntity>){
		this.data = data
		notifyDataSetChanged()
	}


	inner class PlacesCategoryDetailedViewHolder(private val viewBinding: ItemCategoryDetailedBinding):
			BaseViewHolder<PlaceEntity>(viewBinding.root) {


		@ExperimentalStdlibApi
		override fun bind(item: PlaceEntity) {
			if (item.short_title.isNotBlank()) viewBinding.tvTitle.text = item.short_title.capitalizeRu()
			else viewBinding.tvTitle.text = item.title.capitalizeRu()
			//needed to clear recycler views that has already loaded image previously
			viewBinding.ivImageHolder.setImageResource(R.drawable.placeholder)
			//loading image from url
			ImageLoader.with(viewBinding.ivImageHolder.context)
				.load(viewBinding.ivImageHolder, item.images[0].image)
		}


	}

}