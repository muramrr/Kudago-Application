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

import com.mmdev.kudago.app.data.BaseRepository
import com.mmdev.kudago.app.data.api.PlacesApi
import com.mmdev.kudago.app.data.db.FavouritesDao
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.core.SimpleResult
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.domain.places.PlacesResponse

/**
 * [IPlacesRepository] implementation
 */

class PlacesRepositoryImpl(
	private val placesApi: PlacesApi,
	private val favouritesDao: FavouritesDao
) : BaseRepository(), IPlacesRepository {

	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	
	private var firstPageInPool = 1
	private var lastPageInPool = 1
	
	private var category = ""
	private var city = ""


	override suspend fun addPlaceToFavouritesList(
		placeDetailedEntity: PlaceDetailedEntity
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.insertFavourite(placeDetailedEntity.mapToFavourite())
	}

	override suspend fun loadFirstPlaces(
		city: String,
		category: String
	): SimpleResult<PlacesResponse> {
		this.city = city
		this.category = category
		return safeCall(TAG) { placesApi.getPlacesList(unixTime, category, city) }.fold(
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
	
	override suspend fun loadPreviousPlaces(): SimpleResult<PlacesResponse> = safeCall(TAG) {
		placesApi.getPlacesList(unixTime, category, city, firstPageInPool - 1)
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

	override suspend fun loadNextPlaces(): SimpleResult<PlacesResponse> = safeCall(TAG) {
		placesApi.getPlacesList(unixTime, category, city, lastPageInPool + 1)
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

	override suspend fun getPlaceDetails(id: Int): SimpleResult<PlaceDetailedEntity> = safeCall(TAG) {
		placesApi.getPlaceDetails(id)
	}.fold(
		success = {
			if (it.short_title.isBlank() && it.title.isNotBlank())
				it.short_title = it.title
			else
				if (it.title.isBlank() && it.short_title.isNotBlank())
					it.title = it.short_title
			//check if this place is saved to favourites
			val isFavourite = favouritesDao.getFavouritePlace(id)?.mapToPlaceDetailedEntity()
			isFavourite?.let { entity ->
				entity.run { this.isAddedToFavourites = compareId(this.id, isFavourite.id) }
			}
			ResultState.success(it)
		},
		failure = {
			ResultState.failure(it)
		}
	)
	override suspend fun removePlaceFromFavouritesList(
		placeDetailedEntity: PlaceDetailedEntity
	): SimpleResult<Unit> = safeCall(TAG) {
		favouritesDao.deleteFavourite(placeDetailedEntity.mapToFavourite())
	}

}
