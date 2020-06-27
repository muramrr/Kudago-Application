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
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.ItemCategoryEventsBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.events.categories.EventsCategoriesAdapter.AdapterEventsCategory

/**
 * This is the documentation block about the class
 */

class EventsCategoriesAdapter (private val data: List<AdapterEventsCategory> = listOf(
		AdapterEventsCategory("Кино", R.drawable.events_cinema, "cinema"),
		AdapterEventsCategory("Театр", R.drawable.events_theater,"theater"),
		AdapterEventsCategory("Концерты", R.drawable.events_concert,"concert"),
		AdapterEventsCategory("Развлечения", R.drawable.events_entertainment,"entertainment"),
		AdapterEventsCategory("Выставки", R.drawable.events_exhibition,"exhibition"),
		AdapterEventsCategory("Фестивали", R.drawable.events_festival,"festival"),
		AdapterEventsCategory("Вечеринки", R.drawable.events_party,"party"),
		AdapterEventsCategory("Ярмарки", R.drawable.events_fair,"yarmarki-razvlecheniya-yarmarki"),
		AdapterEventsCategory("Другое", R.drawable.events_other,"other"))):
		BaseAdapter<AdapterEventsCategory>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsCategoriesViewHolder =
		EventsCategoriesViewHolder(ItemCategoryEventsBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]

	override fun setData(data: List<AdapterEventsCategory>) {
		//do nothing
	}



	inner class EventsCategoriesViewHolder(private val viewBinding: ItemCategoryEventsBinding):
			BaseViewHolder<AdapterEventsCategory>(viewBinding.root) {

		override fun bind(item: AdapterEventsCategory){
			viewBinding.ivEventsCategoryIcon.setImageResource(item.picture)
		}

	}

	data class AdapterEventsCategory(val title: String, val picture: Int, val apiIdentifier: String)
}