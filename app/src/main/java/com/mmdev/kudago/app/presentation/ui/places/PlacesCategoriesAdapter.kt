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

package com.mmdev.kudago.app.presentation.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmdev.kudago.app.databinding.FragmentRvPlacesCategoryItemBinding


/**
 * This is the documentation block about the class
 */

class PlacesCategoriesAdapter (private val data: List<String> = emptyList()):

		RecyclerView.Adapter<PlacesCategoriesAdapter.PlacesCategoriesViewHolder>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesCategoriesViewHolder =
		PlacesCategoriesViewHolder(FragmentRvPlacesCategoryItemBinding
			                           .inflate(LayoutInflater.from(parent.context),
			                                    parent,
			                                    false)
		)

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: PlacesCategoriesViewHolder, position: Int) {
		holder.bind(data[position])
	}

	fun setData(data: List<String>){

		notifyDataSetChanged()
	}


	inner class PlacesCategoriesViewHolder(private val view: FragmentRvPlacesCategoryItemBinding):
			RecyclerView.ViewHolder(view.root) {


		fun bind(item: String){

		}


	}


}