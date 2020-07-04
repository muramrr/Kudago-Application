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

package com.mmdev.kudago.app.presentation.ui.events.event_detailed

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.ItemEventDetailedDateItemBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.ui.events.event_detailed.EventDetailedDatesAdapter.DateHuman

/**
 * This is the documentation block about the class
 */

class EventDetailedDatesAdapter(private var data: List<DateHuman> = emptyList()): BaseAdapter<DateHuman>() {


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		EventsCategoryDetailedViewHolder(
				ItemEventDetailedDateItemBinding.inflate(LayoutInflater.from(parent.context),
				                                              parent,
				                                              false)
		)

	override fun getItemCount(): Int = data.size
	override fun getItem(position: Int) = data[position]


	override fun setData(data: List<DateHuman>){
		this.data = data
		notifyDataSetChanged()
	}


	inner class EventsCategoryDetailedViewHolder (private val viewBinding: ItemEventDetailedDateItemBinding):
			BaseViewHolder<DateHuman>(viewBinding.root) {


		override fun bind(item: DateHuman) {
			if (item.getDate().isNotBlank()) {
				viewBinding.tvDetailedDate.text = item.getDate()
			}
			else viewBinding.tvDetailedDate.run {
				this.text = this.context.resources.getString(R.string.event_detailed_whole_year)
			}
			when {
				item.getTime().isBlank() -> viewBinding.tvDetailedTime.run {
					this.text = this.context.resources.getString(R.string.event_detailed_whole_day)
				}

				item.getTime() in arrayOf("0:00", "12:00 AM") -> viewBinding.tvDetailedTime.run {
					this.text = this.context.resources.getString(R.string.event_detailed_every_day)
				}

				else -> viewBinding.tvDetailedTime.text = item.getTime()
			}

		}
	}

	data class DateHuman(val startDate: String = "", val startTime: String = "",
	                     val endDate: String? = null, val endTime: String? = null) {

		fun getDate(): String {
			if (endDate != null )
				if (startDate != endDate) return "$startDate - $endDate"
			return startDate
		}

		fun getTime(): String {
			if (endDate != null)
				if (startTime != endTime) return "$startTime - $endTime"
			return startTime
		}
	}

}