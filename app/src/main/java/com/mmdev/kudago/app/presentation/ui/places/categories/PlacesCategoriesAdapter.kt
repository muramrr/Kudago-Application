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

package com.mmdev.kudago.app.presentation.ui.places.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.ItemCategoryBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.places.categories.PlacesCategoriesAdapter.AdapterPlaceCategory


/**
 * This is the documentation block about the class
 */

class PlacesCategoriesAdapter (private val data: List<AdapterPlaceCategory> = listOf(
		AdapterPlaceCategory("Рестораны и кафе", R.drawable.ic_places_restaurants_24dp, "restaurants"),
		AdapterPlaceCategory("Бары", R.drawable.ic_places_bar_24dp, "bar"),
		AdapterPlaceCategory("Развлечения", R.drawable.ic_places_amusement_24dp, "amusement"),
		AdapterPlaceCategory("Антикафе", R.drawable.ic_places_anticafe_24dp, "anticafe"),
		AdapterPlaceCategory("Арт-центры", R.drawable.ic_places_artcenter_24dp, "art-centers"),
		AdapterPlaceCategory("Арт-пространства", R.drawable.ic_places_artspace_24dp, "art-space"),
		AdapterPlaceCategory("Кинотеатры", R.drawable.ic_places_cinema_24dp, "cinema"),
		AdapterPlaceCategory("Театры", R.drawable.ic_places_theater_24dp, "theatre"),
		AdapterPlaceCategory("Музеи", R.drawable.ic_places_museums_24dp, "museums"),
		AdapterPlaceCategory("Парки", R.drawable.ic_places_park_24dp, "park"),
		AdapterPlaceCategory("Клубы", R.drawable.ic_places_clubs_24dp, "clubs"),
		AdapterPlaceCategory("Стрип-клубы", R.drawable.ic_places_stripclub_24dp, "strip-club"),
		AdapterPlaceCategory("Интересные места", R.drawable.ic_places_sights_24dp, "sights"),
		AdapterPlaceCategory("Другое", R.drawable.ic_places_other_24dp, "other"))) :

		BaseAdapter<AdapterPlaceCategory>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesCategoriesViewHolder =
		PlacesCategoriesViewHolder(ItemCategoryBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]

	override fun setData(data: List<AdapterPlaceCategory>) {}



	inner class PlacesCategoriesViewHolder(private val viewBinding: ItemCategoryBinding):
			BaseViewHolder<AdapterPlaceCategory>(viewBinding.root) {


		override fun bind(item: AdapterPlaceCategory){

			viewBinding.tvCategoryTitle.text = item.title
			viewBinding.ivCategoryIcon.setImageResource(item.icon)
		}


	}

	data class AdapterPlaceCategory(val title: String, val icon: Int, val apiIdentifier: String)

}