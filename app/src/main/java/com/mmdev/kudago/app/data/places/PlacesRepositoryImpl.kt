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

import com.mmdev.kudago.app.data.BaseRepository
import com.mmdev.kudago.app.data.api.PlacesApi
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.domain.places.PlacesResponse

/**
 * This is the documentation block about the class
 */

class PlacesRepositoryImpl (private val placesApi: PlacesApi) : BaseRepository(), IPlacesRepository {

	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	private var page = 1

	private var category = ""


	override suspend fun addPlaceToFavouritesList(placeEntity: PlaceEntity): ResultState<Unit> {
		TODO("Not yet implemented")
	}

	override suspend fun loadFirstPlaces(category: String): ResultState<PlacesResponse> {
		this.category = category
		//another approach
//		val placesResponse = safeApiCall(
//				call = { placesApi.getPlacesListAsync(unixTime, category, "msk").await() },
//				errorMessage = "Error Loading Places"
//		)
//
//		return placesResponse.results.toMutableList()
		return try {
			page = 1

			val result = placesApi.getPlacesListAsync(unixTime, category, "msk")
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}

	override suspend fun loadMorePlaces(): ResultState<PlacesResponse> {
		return try {
			page++
			val result = placesApi.getPlacesListAsync(unixTime, category, "msk", page)
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}

	override suspend fun getPlaceDetails(id: Int): ResultState<PlaceDetailedEntity> =
		try {
			val result = placesApi.getPlaceDetailsAsync(id)
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}

	override suspend fun removePlaceFromFavouritesList(placeEntity: PlaceEntity): ResultState<Unit> {
		TODO("Not yet implemented")
	}

}
