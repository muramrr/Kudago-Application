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

import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.IEventsRepository
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * This is the documentation block about the class
 */

class EventDetailedPresenter (private val repository: IEventsRepository) :
		BasePresenter<EventDetailedContract.View>(),
		EventDetailedContract.Presenter {

	private lateinit var eventDetailedEntity: EventDetailedEntity

	private var isAdded = false

	override fun addOrRemoveEventToFavourites() {
		launch {
			val result = withContext(coroutineContext) {
				if (isAdded) repository.removeEventFromFavouritesList(eventDetailedEntity)
				else repository.addEventToFavouritesList(eventDetailedEntity)
			}
			when (result) {
				is ResultState.Success -> {
					isAdded = if (isAdded){
						getLinkedView()?.showSuccessDeletedToast()
						false
					}
					else {
						getLinkedView()?.showSuccessAddedToast()
						true
					}
					handleFabState(isAdded)

				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

		}
	}

	@ExperimentalStdlibApi
	override fun loadEventDetailsById(id: Int) {
		launch {
			val result = withContext(coroutineContext) {
				repository.getEventDetails(id)
			}
			when (result) {
				is ResultState.Success -> {
					eventDetailedEntity = result.data
					getLinkedView()?.updateData(eventDetailedEntity)
					getLinkedView()?.setEventTime(convertTime(eventDetailedEntity.dates[0].start,
					                                          eventDetailedEntity.dates[0].end))
					handleFabState(eventDetailedEntity.isAddedToFavourites)

					isAdded = eventDetailedEntity.isAddedToFavourites
				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}
		}
	}

	private fun handleFabState(added: Boolean) {
		if (added) getLinkedView()?.setRemoveTextFab()
		else getLinkedView()?.setAddTextFab()
	}

	//bad code
	private fun convertTime(start: Long, end: Long): DateHuman? {
		val timeFormatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
		val dateFormatter = SimpleDateFormat("EEE, dd MMMM, YYYY", Locale.getDefault())

		var fStart: Date? = null
		if (start != -62135433000)
			fStart = Date(start * 1000L)

		var fEnd: Date? = null
		if (end != 253370754000)
			fEnd = Date(end * 1000L)

		return when {
			fStart != null && fEnd != null -> DateHuman(dateFormatter.format(fStart),
			                                            timeFormatter.format(fStart),
			                                            dateFormatter.format(fEnd),
			                                            timeFormatter.format(fEnd))

			fStart != null && fEnd == null -> DateHuman(dateFormatter.format(fStart),
			                                            timeFormatter.format(fStart))

			else -> null
		}
	}

	data class DateHuman(val startDate: String, val startTime: String,
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