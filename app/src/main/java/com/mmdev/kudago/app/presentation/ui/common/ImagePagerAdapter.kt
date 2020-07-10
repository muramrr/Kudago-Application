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

package com.mmdev.kudago.app.presentation.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.core.utils.image_loader.load
import com.mmdev.kudago.app.databinding.ItemImagePagerBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter


class ImagePagerAdapter (private var data: List<String> = emptyList()) :
		BaseAdapter<String>(){


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		PlacesCategoryDetailedViewHolder(
				ItemImagePagerBinding.inflate(LayoutInflater.from(parent.context),
				                                         parent,
				                                         false)
		)

	override fun getItem(position: Int) = data[position]
	override fun getItemCount() = data.size

	override fun setData(data: List<String>){
		this.data = data
		notifyDataSetChanged()
	}


	inner class PlacesCategoryDetailedViewHolder (private val viewBinding: ItemImagePagerBinding):
			BaseViewHolder<String>(viewBinding.root) {


		override fun bind(item: String) {
			//Picasso.get().load(item.images[0].image).into(viewBinding.ivImageHolder)
			viewBinding.ivPhoto.load(item)
		}


	}
}