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
import com.mmdev.kudago.app.presentation.ui.events.categories.EventsCategoriesAdapter.EventsCategory

/**
 * This is the documentation block about the class
 */

class EventsCategoriesAdapter (private val data: List<EventsCategory> = listOf(
		EventsCategory("cinema", R.drawable.events_cinema),
		EventsCategory("theater", R.drawable.events_theater),
		EventsCategory("concert", R.drawable.events_concert),
		EventsCategory("entertainment", R.drawable.events_entertainment),
		EventsCategory("exhibition", R.drawable.events_exhibition),
		EventsCategory("festival", R.drawable.events_festival),
		EventsCategory("party", R.drawable.events_party),
		EventsCategory("yarmarki-razvlecheniya-yarmarki", R.drawable.events_fair),
		EventsCategory("other", R.drawable.events_other))): BaseAdapter<EventsCategory>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsCategoriesViewHolder =
		EventsCategoriesViewHolder(ItemCategoryEventsBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]

	override fun setData(data: List<EventsCategory>) {
		//do nothing
	}



	inner class EventsCategoriesViewHolder(private val viewBinding: ItemCategoryEventsBinding):
			BaseViewHolder<EventsCategory>(viewBinding.root) {

		override fun bind(item: EventsCategory){
			viewBinding.ivEventsCategoryIcon.setImageResource(item.picture)
		}

	}

	data class EventsCategory(val title: String, val picture: Int)
}