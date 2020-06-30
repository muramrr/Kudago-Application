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

package com.mmdev.kudago.app.presentation.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.databinding.ItemCategoryBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.base.CategoriesAdapter.AdapterCategoryItem


/**
 * This is the documentation block about the class
 */

class CategoriesAdapter (private val data: List<AdapterCategoryItem>) :

		BaseAdapter<AdapterCategoryItem>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesCategoriesViewHolder =
		PlacesCategoriesViewHolder(ItemCategoryBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]

	override fun setData(data: List<AdapterCategoryItem>) {}



	inner class PlacesCategoriesViewHolder(private val viewBinding: ItemCategoryBinding):
			BaseViewHolder<AdapterCategoryItem>(viewBinding.root) {


		override fun bind(item: AdapterCategoryItem){

			viewBinding.tvCategoryTitle.text = item.title
			viewBinding.ivCategoryIcon.setImageResource(item.icon)
		}


	}

	data class AdapterCategoryItem(val title: String = "",
	                               val icon: Int = 0,
	                               val apiIdentifier: String = "")

}