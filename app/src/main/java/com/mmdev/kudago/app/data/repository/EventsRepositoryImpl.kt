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

package com.mmdev.kudago.app.data.repository

import com.mmdev.kudago.app.core.utils.log.logInfo
import com.mmdev.kudago.app.data.BaseRepository
import com.mmdev.kudago.app.data.api.EventsApi
import com.mmdev.kudago.app.data.api.EventsResponse
import com.mmdev.kudago.app.data.db.FavouritesDao
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.core.SimpleResult
import com.mmdev.kudago.app.domain.events.IEventsRepository
import com.mmdev.kudago.app.domain.events.data.EventDetailedInfo
import com.mmdev.kudago.app.domain.favourites.FavouriteType

/**
 * [IEventsRepository] implementation
 */

class EventsRepositoryImpl(
	private val eventsApi: EventsApi,
	private val favouritesDao: FavouritesDao
): BaseRepository(), IEventsRepository {
	
	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	
	private var firstPageInPool = 1
	private var lastPageInPool = 1

	private var category = ""
	private var city = ""
	

	override suspend fun addEventToFavouritesList(
		eventDetailedInfo: EventDetailedInfo
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.insertFavourite(eventDetailedInfo.mapToFavourite())
	}

	override suspend fun loadFirstEvents(
		city: String,
		category: String
	): SimpleResult<EventsResponse> {
		logInfo(TAG, "Loading first events")
		this.city = city
		this.category = category
		return safeCall(TAG) { eventsApi.getEventsList(unixTime, category, city) }.fold(
			success = {
				firstPageInPool = 1
				lastPageInPool = 1
				ResultState.success(it)
			},
			failure = {
				ResultState.failure(it)
			}
		)
	}
	
	override suspend fun loadPreviousEvents(): SimpleResult<EventsResponse> = safeCall(TAG) {
		eventsApi.getEventsList(unixTime, category, city, firstPageInPool - 1)
	}.fold(
		success = {
			firstPageInPool--
			if (firstPageInPool > 0) lastPageInPool--
			ResultState.success(it)
		},
		failure = {
			ResultState.failure(it)
		}
	)
	
	override suspend fun loadNextEvents(): SimpleResult<EventsResponse> = safeCall(TAG) {
		eventsApi.getEventsList(unixTime, category, city, lastPageInPool + 1)
	}.fold(
		success = {
			lastPageInPool++
			if (lastPageInPool > 2) firstPageInPool++
			ResultState.success(it)
		},
		failure = {
			ResultState.failure(it)
		}
	)
	

	override suspend fun getEventDetails(id: Int): SimpleResult<EventDetailedInfo> = safeCall(TAG) {
		eventsApi.getEventDetails(id)
	}.fold(
		success = {
			var eventDetailed = it
			if (it.short_title.isBlank() && it.title.isNotBlank())
				eventDetailed = eventDetailed.copy(short_title = it.title)
			else
				if (it.title.isBlank() && it.short_title.isNotBlank())
					eventDetailed = eventDetailed.copy(title = it.short_title)
			//check if this place is saved to favourites
			val isFavourite = favouritesDao.getFavouriteById(FavouriteType.EVENT.name, id) != null
			eventDetailed = eventDetailed.copy(isAddedToFavourites = isFavourite)
			ResultState.success(eventDetailed)
		},
		failure = {
			ResultState.failure(it)
		}
	)

	override suspend fun removeEventFromFavouritesList(
		eventDetailedInfo: EventDetailedInfo
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.deleteFavourite(eventDetailedInfo.id)
	}


}