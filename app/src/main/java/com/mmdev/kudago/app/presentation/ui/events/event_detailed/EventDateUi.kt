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

/**
 * event date in human format
 */

data class EventDateUi(
	val startDate: String = "",
	val startTime: String = "",
	val endDate: String? = null,
	val endTime: String? = null,
	val startInMillis: Long = 0,
	val endInMillis: Long = 0) {
	
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