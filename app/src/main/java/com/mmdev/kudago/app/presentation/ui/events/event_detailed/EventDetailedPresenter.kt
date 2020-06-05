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
import com.mmdev.kudago.app.presentation.base.BasePresenter
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

	override fun addEventToFavourites() {
		launch {
			val result = withContext(coroutineContext) {
				if (isAdded) repository.addEventToFavouritesList(eventDetailedEntity)
				else repository.removeEventFromFavouritesList(eventDetailedEntity)
			}
			when (result) {
				is ResultState.Success -> {
					isAdded = if (isAdded){
						getLinkedView()?.showToast("Successfully removed from favourites")
						false
					}
					else {
						getLinkedView()?.showToast("Successfully added to favourites")
						true
					}
					getLinkedView()?.updateFabButton(isAdded)

				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

		}
	}

	override fun loadEventDetailsById(id: Int) {
		launch {
			val result = withContext(coroutineContext) {
				repository.getEventDetails(id)
			}
			when (result) {
				is ResultState.Success -> {
					getLinkedView()?.updateData(result.data)
					eventDetailedEntity = result.data
				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}
		}
	}


}