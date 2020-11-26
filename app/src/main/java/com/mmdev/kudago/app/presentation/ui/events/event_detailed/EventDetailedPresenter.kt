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
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.IEventsRepository
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
			withContext(coroutineContext) {
				if (isAdded) repository.removeEventFromFavouritesList(eventDetailedEntity)
				else repository.addEventToFavouritesList(eventDetailedEntity)
			}.fold(
				success = {
					isAdded = if (isAdded){
						getLinkedView()?.showSuccessDeletedToast()
						false
					}
					else {
						getLinkedView()?.showSuccessAddedToast()
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
					eventDetailedEntity = it
					getLinkedView()?.updateData(it)
					getLinkedView()?.setEventDateTime(it.mapToUIEventDateList())
					
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
		if (added) getLinkedView()?.setRemoveTextFab()
		else getLinkedView()?.setAddTextFab()
	}

}