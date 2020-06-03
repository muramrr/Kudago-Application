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

package com.mmdev.kudago.app.presentation.ui.events.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.databinding.ItemCategoryBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter

/**
 * This is the documentation block about the class
 */

class EventsCategoriesAdapter (private val data: List<String> =
	                               listOf("cinema", "concert", "entertainment",
	                                      "exhibition", "festival", "other",
	                                      "party", "stock", "theater",
	                                      "yarmarki-razvlecheniya-yarmarki"
	                               )):

		BaseAdapter<String>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesCategoriesViewHolder =
		PlacesCategoriesViewHolder(ItemCategoryBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]

	override fun setData(data: List<String>) {
		//do nothing
	}



	inner class PlacesCategoriesViewHolder(private val viewBinding: ItemCategoryBinding):
			BaseViewHolder<String>(viewBinding.root) {

		override fun bind(item: String){
			viewBinding.tvCategoryTitle.text = item
		}

	}

}