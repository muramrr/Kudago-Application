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
import androidx.recyclerview.widget.RecyclerView
import com.mmdev.kudago.app.databinding.ItemPlaceCategoryDetailedBinding
import com.mmdev.kudago.app.domain.places.PlaceEntity

/**
 * This is the documentation block about the class
 */

class PlacesCategoryDetailedAdapter (private var data: List<PlaceEntity> = emptyList()):

		RecyclerView.Adapter<PlacesCategoryDetailedAdapter.PlacesCategoryDetailedViewHolder>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		PlacesCategoryDetailedViewHolder(
				ItemPlaceCategoryDetailedBinding.inflate(LayoutInflater.from(parent.context),
				                                         parent,
				                                         false)
		)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: PlacesCategoryDetailedViewHolder, position: Int) {
		holder.bind(data[position])
	}


	fun setData(data: List<PlaceEntity>){
		this.data = data
		notifyDataSetChanged()
	}


	inner class PlacesCategoryDetailedViewHolder (private val viewBinding:
	                                              ItemPlaceCategoryDetailedBinding):
			RecyclerView.ViewHolder(viewBinding.root) {


		fun bind(item: PlaceEntity) {
			viewBinding.tvPlaceTitle.text = item.short_title
		}


	}

}