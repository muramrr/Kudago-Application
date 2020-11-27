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

import com.mmdev.kudago.app.core.utils.log.logError
import com.mmdev.kudago.app.domain.events.IEventsRepository
import com.mmdev.kudago.app.domain.events.data.EventDate
import com.mmdev.kudago.app.domain.events.data.EventDetailedInfo
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

	private lateinit var eventDetailedInfo: EventDetailedInfo

	private var isAdded = false

	override fun addOrRemoveEventToFavourites() {
		launch {
			withContext(coroutineContext) {
				if (isAdded) repository.removeEventFromFavouritesList(eventDetailedInfo)
				else repository.addEventToFavouritesList(eventDetailedInfo)
			}.fold(
				success = {
					isAdded = if (isAdded){
						attachedView?.showSuccessDeletedToast()
						false
					}
					else {
						attachedView?.showSuccessAddedToast()
						true
					}
					handleFabState(isAdded)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)

		}
	}

	override fun loadEventDetailsById(id: Int) {
		launch {
			withContext(coroutineContext) {
				repository.getEventDetails(id)
			}.fold(
				success = {
					eventDetailedInfo = it
					attachedView?.updateData(it)
					attachedView?.setEventDateTime(mapEventDatesToUi(it))
					
					handleFabState(it.isAddedToFavourites)
					
					isAdded = it.isAddedToFavourites
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
			
		}
	}

	private fun handleFabState(added: Boolean) {
		if (added) attachedView?.setRemoveTextFab()
		else attachedView?.setAddTextFab()
	}
	
	private fun mapEventDatesToUi(input: EventDetailedInfo): List<EventDateUi> {
		return input.dates
			//filter dates which end value bigger than current time
			.filter{ eventDate -> eventDate.end > System.currentTimeMillis() / 1000L }
			//map filtered items
			.map{ eventDate -> mapToUIEventDate(eventDate) }
	}
	
	private fun mapToUIEventDate(input: EventDate) : EventDateUi {
		
		val timeFormatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
		val dateFormatter = SimpleDateFormat("EEE, dd MMMM, yyyy", Locale.getDefault())
		
		//check if start is defined correctly
		var fStart: Date? = null
		if (input.start != -62135433000) //03.01.0001
			fStart = Date(input.start * 1000L)
		
		//check if end is defined correctly
		var fEnd: Date? = null
		if (input.end != 253370754000) //01.01.9999
			fEnd = Date(input.end * 1000L)
		
		return when {
			//both are properly defined
			fStart != null && fEnd != null -> EventDateUi(
				dateFormatter.format(fStart),
				timeFormatter.format(fStart),
				dateFormatter.format(fEnd),
				timeFormatter.format(fEnd),
				fStart.time, fEnd.time
			)
			
			//end is undefined
			fStart != null && fEnd == null -> EventDateUi(
				startDate = dateFormatter.format(fStart),
				startTime = timeFormatter.format(fStart),
				startInMillis = fStart.time
			)
			
			//start & end is undefined
			else -> EventDateUi()
		}
		
	}

}