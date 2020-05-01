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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * This is the documentation block about the class
 */

abstract class BaseAdapter<T>: RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder<T>>() {

	private var mClickListener: OnItemClickListener<T>? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		BaseViewHolder<T>(LayoutInflater.from(parent.context)
			                  .inflate(viewType,
			                           parent,
			                           false))

	override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
		holder.bind(getItem(position))

	override fun getItemViewType(position: Int) = getLayoutIdForItem(position)

	abstract fun getItem(position: Int): T
	abstract fun getLayoutIdForItem(position: Int): Int

	// allows clicks events to be caught
	open fun setOnItemClickListener(itemClickListener: OnItemClickListener<T>) {
		mClickListener = itemClickListener
	}

	override fun onFailedToRecycleView(holder: BaseViewHolder<T>): Boolean { return true }

	inner class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {

		init {
			mClickListener?.let { mClickListener ->
				itemView.setOnClickListener {
					mClickListener.onItemClick(getItem(adapterPosition), adapterPosition)
				}
			}
		}

		fun bind(item: T) {

		}
	}

	// parent fragment will override this method to respond to click events
	interface OnItemClickListener<T> {
		fun onItemClick(item: T, position: Int)
	}

	interface BindableAdapter<T> {
		fun setData(data: T)
	}
}