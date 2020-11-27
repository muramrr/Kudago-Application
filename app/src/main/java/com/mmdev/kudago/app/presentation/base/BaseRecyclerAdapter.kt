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

abstract class BaseRecyclerAdapter<T>: RecyclerView.Adapter<BaseViewHolder<T>>() {

	override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
		holder.bind(getItem(position))

//	override fun onFailedToRecycleView(holder: BaseViewHolder<T>): Boolean { return true }
	
	abstract fun getItem(position: Int): T
	
	open fun updateData(data: List<T>) {}
	
	protected var mClickListener: ((view: View, position: Int, item: T) -> Unit)? = null
	
	// allows clicks events to be caught
	open fun setOnItemClickListener(listener: (view: View, position: Int, item: T) -> Unit) {
		mClickListener = listener
	}
	
}