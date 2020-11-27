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

package com.mmdev.kudago.app.presentation.ui.places.place_detailed

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.databinding.ItemPlacePhoneBinding
import com.mmdev.kudago.app.presentation.base.BaseRecyclerAdapter
import com.mmdev.kudago.app.presentation.base.BaseViewHolder

/**
 *
 */

class PhoneAdapter(
	private var data: List<String> = emptyList()
): BaseRecyclerAdapter<String>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> =
		PhoneNumberViewHolder(
			ItemPlacePhoneBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	
	override fun getItem(position: Int): String = data[position]
	override fun getItemCount(): Int = data.size
	
	override fun updateData(data: List<String>) {
		this.data = data
		notifyDataSetChanged()
	}
	
	inner class PhoneNumberViewHolder(
		private val viewBinding: ItemPlacePhoneBinding
	) : BaseViewHolder<String>(viewBinding) {
		
		init {
			mClickListener?.let { listener ->
				itemView.setOnClickListener {
					listener.invoke(it, adapterPosition, getItem(adapterPosition))
				}
			}
		}
		
		override fun bind(item: String) {
			viewBinding.btnPhoneNumber.text = item
		}
		
		
	}
	
}