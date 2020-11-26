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
import com.mmdev.kudago.app.data.db.FavouritesDao
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.core.SimpleResult
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.EventsResponse
import com.mmdev.kudago.app.domain.events.IEventsRepository

/**
 * [IEventsRepository] implementation
 */

class EventsRepositoryImpl(
	private val eventsApi: EventsApi,
	private val favouritesDao: FavouritesDao
): BaseRepository(), IEventsRepository {
	
	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	private var page = 1
		private set(value) {
			field = if (value < 1) 1
			else value
		}

	private var category = ""
	private var city = ""
	

	override suspend fun addEventToFavouritesList(
		eventDetailedEntity: EventDetailedEntity
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.insertFavourite(eventDetailedEntity.mapToFavourite())
	}

	override suspend fun loadFirstEvents(
		city: String,
		category: String
	): SimpleResult<EventsResponse> {
		logInfo(TAG, "Loading first events")
		this.city = city
		this.category = category
		page = 1
		return safeCall(TAG) { eventsApi.getEventsList(unixTime, category, city) }.fold(
			success = {
				page++
				ResultState.success(it)
			},
			failure = {
				ResultState.failure(it)
			}
		)
	}
	
	override suspend fun loadPreviousEvents(): SimpleResult<EventsResponse> = safeCall(TAG) {
		logInfo(TAG, "Loading previous events, page = $page")
		eventsApi.getEventsList(unixTime, category, city, page)
	}.fold(
		success = {
			page--
			ResultState.success(it)
		},
		failure = {
			page++
			ResultState.failure(it)
		}
	)
	
	override suspend fun loadNextEvents(): SimpleResult<EventsResponse> = safeCall(TAG) {
		logInfo(TAG, "Loading next events, page = $page")
		eventsApi.getEventsList(unixTime, category, city, page)
	}.fold(
		success = {
			page++
			ResultState.success(it)
		},
		failure = {
			page--
			ResultState.failure(it)
		}
	)
	

	override suspend fun getEventDetails(id: Int): SimpleResult<EventDetailedEntity> = safeCall(TAG) {
		eventsApi.getEventDetails(id)
	}.fold(
		success = {
			if (it.short_title.isBlank() && it.title.isNotBlank())
				it.short_title = it.title
			else
				if (it.title.isBlank() && it.short_title.isNotBlank())
					it.title = it.short_title
			val isFavourite = favouritesDao.getFavouriteEvent(id)?.mapToEventDetailedEntity()
			isFavourite?.let { entity ->
				entity.run { this.isAddedToFavourites = compareId(this.id, isFavourite.id) }
			}
			ResultState.success(it)
		},
		failure = {
			ResultState.failure(it)
		}
	)

	override suspend fun removeEventFromFavouritesList(
		eventDetailedEntity: EventDetailedEntity
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.deleteFavourite(eventDetailedEntity.mapToFavourite())
	}


}