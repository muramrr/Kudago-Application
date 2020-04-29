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

package com.mmdev.kudago.app.data.places

import com.mmdev.kudago.app.data.api.PlacesApi
import com.mmdev.kudago.app.domain.core.UseCaseResult
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.domain.places.PlacesResponse
import com.mmdev.kudago.app.domain.places.IPlacesRepository

/**
 * This is the documentation block about the class
 */

class PlacesRepositoryImpl (private val placesApi: PlacesApi) :
		IPlacesRepository {

	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	private var page = 1


	override suspend fun addPlaceToFavouritesList(placeEntity: PlaceEntity): UseCaseResult<Unit> {
		TODO("Not yet implemented")
	}

	override suspend fun loadFirstPlaces(category: String): UseCaseResult<PlacesResponse> {
		return try {
			page = 0

			val result = placesApi.getPlacesListAsync(unixTime, category, "msk").await()
			UseCaseResult.Success(result)
		}
		catch (ex: Exception) {
			UseCaseResult.Error(ex)
		}
	}

	override suspend fun loadMorePlaces(category: String): UseCaseResult<PlacesResponse> {
		return try {
			page++
			val result = placesApi.getPlacesListAsync(unixTime, category, "msk", page).await()
			UseCaseResult.Success(result)
		}
		catch (ex: Exception) {
			UseCaseResult.Error(ex)
		}
	}

	override suspend fun getPlaceDetails(id: Int): UseCaseResult<PlaceDetailedEntity> =
		try {
			val result = placesApi.getPlaceDetailsAsync(id).await()
			UseCaseResult.Success(result)
		}
		catch (ex: Exception) {
			UseCaseResult.Error(ex)
		}

	override suspend fun removePlaceFromFavouritesList(placeEntity: PlaceEntity): UseCaseResult<Unit> {
		TODO("Not yet implemented")
	}

}
