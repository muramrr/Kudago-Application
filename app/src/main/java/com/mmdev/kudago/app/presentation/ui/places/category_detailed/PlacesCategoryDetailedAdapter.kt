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
import com.mmdev.kudago.app.core.utils.image_loader.ImageLoader
import com.mmdev.kudago.app.databinding.ItemCategoryDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedAdapter (private val placesList: MutableList<PlaceEntity> = mutableListOf()):
		BaseAdapter<PlaceEntity>() {

	private var startPos = 0

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		PlacesCategoryDetailedViewHolder(
				ItemCategoryDetailedBinding.inflate(LayoutInflater.from(parent.context),
				                                    parent,
				                                    false)
		)

	override fun getItemCount(): Int = placesList.size
	override fun getItem(position: Int) = placesList[position]


	override fun setData(data: List<PlaceEntity>){
		placesList.addAll(data)
		notifyItemRangeInserted(startPos, data.size)
	}

	fun updateData(data: List<PlaceEntity>) {
		startPos = placesList.size
		placesList.addAll(data)
		notifyItemRangeInserted(startPos, data.size)
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
			ImageLoader.get()
				.load(viewBinding.ivImageHolder, item.images[0].image)
		}


	}

}