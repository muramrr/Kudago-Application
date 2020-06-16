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

package com.mmdev.kudago.app.data.events

import com.mmdev.kudago.app.data.BaseRepository
import com.mmdev.kudago.app.data.api.EventsApi
import com.mmdev.kudago.app.data.favourites.db.FavouritesDao
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.EventsResponse
import com.mmdev.kudago.app.domain.events.IEventsRepository

/**
 * [IEventsRepository] implementation
 */

class EventsRepositoryImpl (private val eventsApi: EventsApi,
                            private val favouritesDao: FavouritesDao):
		BaseRepository(), IEventsRepository {


	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	private var page = 1

	private var category = ""


	override suspend fun addEventToFavouritesList(eventDetailedEntity: EventDetailedEntity): ResultState<Unit> {
		return try {
			val result = favouritesDao.insertFavourite(eventDetailedEntity.mapToFavourite())
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}

	override suspend fun loadFirstEvents(category: String): EventsResponse? {
		this.category = category
		page = 1
		return safeApiCall(
				call = { eventsApi.getEventsListAsync(unixTime, category, "msk", page = page) },
				errorMessage = "Error Loading Events"
		)
	}

	override suspend fun loadMoreEvents(): EventsResponse? {
		page++
		return safeApiCall(
				call = { eventsApi.getEventsListAsync(unixTime, category, "msk", page = page) },
				errorMessage = "Error Loading More Events"
		)
	}

	override suspend fun getEventDetails(id: Int): ResultState<EventDetailedEntity> {
		return try {
			val result = eventsApi.getEventDetailsAsync(id)
			if (result.short_title.isBlank() && result.title.isNotBlank()) result.short_title = result.title
			else
				if (result.title.isBlank() && result.short_title.isNotBlank()) result.title = result.short_title
			val isFavourite = favouritesDao.getFavouriteEvent(id)?.mapToEventDetailedEntity()
			isFavourite?.let { result.run { this.isAddedToFavourites = compareId(this.id, isFavourite.id) } }
			ResultState.Success(result)
		}
		catch (e: Exception){
			ResultState.Error(e)
		}
	}

	override suspend fun removeEventFromFavouritesList(eventDetailedEntity: EventDetailedEntity): ResultState<Unit> {
		return try {
			val result = favouritesDao.deleteFavourite(eventDetailedEntity.mapToFavourite())
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}


}