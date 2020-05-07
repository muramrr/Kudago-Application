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

package com.mmdev.kudago.app.presentation.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * This is the documentation block about the class
 */

abstract class BaseAdapter<T>: RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder<T>>() {



	abstract fun getItem(position: Int): T

	abstract fun setData(data: List<T>)

	override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
		holder.bind(getItem(position))


	private var mClickListener: OnItemClickListener<T>? = null

	// allows clicks events to be caught
	fun setOnItemClickListener(itemClickListener: OnItemClickListener<T>) {
		mClickListener = itemClickListener
	}

	// parent fragment will override this method to respond to click events
	interface OnItemClickListener<T> {
		fun onItemClick(item: T, position: Int)
	}

	open inner class BaseViewHolder<T>(private val bindingRoot: View):
			RecyclerView.ViewHolder(bindingRoot){

		init {
			bindingRoot.setOnClickListener {
				mClickListener?.onItemClick(getItem(adapterPosition), adapterPosition)
			}
		}

		open fun bind(item: T) {}

	}

}