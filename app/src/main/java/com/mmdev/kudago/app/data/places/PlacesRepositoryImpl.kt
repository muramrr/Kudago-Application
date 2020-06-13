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
import com.mmdev.kudago.app.data.favourites.db.FavouritesDao
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.domain.places.PlacesResponse

/**
 * This is the documentation block about the class
 */

class PlacesRepositoryImpl (private val placesApi: PlacesApi,
                            private val favouritesDao: FavouritesDao) :
		BaseRepository(), IPlacesRepository {

	//current time
	private val unixTime = System.currentTimeMillis() / 1000L
	private var page = 1

	private var category = ""


	override suspend fun addPlaceToFavouritesList(placeDetailedEntity: PlaceDetailedEntity): ResultState<Unit> {
		return try {
			val result = favouritesDao.insertFavourite(placeDetailedEntity.mapToFavourite())
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}

	override suspend fun loadFirstPlaces(category: String): PlacesResponse? {
		this.category = category
		page = 1
		return safeApiCall(
				call = { placesApi.getPlacesListAsync(unixTime, category, "msk", page = page) },
				errorMessage = "Error Loading Places"
		)
	}

	override suspend fun loadMorePlaces(): PlacesResponse? {
		page++
		return safeApiCall(
				call = { placesApi.getPlacesListAsync(unixTime, category, "msk", page = page) },
				errorMessage = "Error Loading More Places"
		)
	}

	override suspend fun getPlaceDetails(id: Int): ResultState<PlaceDetailedEntity> =
		try {
			val result = placesApi.getPlaceDetailsAsync(id)
			//check if this place is saved to favourites
			val isFavourite = favouritesDao.getFavouritePlace(id)?.mapToPlaceDetailedEntity()
			isFavourite?.let { result.run { this.isAddedToFavourites = compareId(this.id, isFavourite.id) } }
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}

	override suspend fun removePlaceFromFavouritesList(placeDetailedEntity: PlaceDetailedEntity): ResultState<Unit> {
		return try {
			val result = favouritesDao.deleteFavourite(placeDetailedEntity.mapToFavourite())
			ResultState.Success(result)
		}
		catch (ex: Exception) {
			ResultState.Error(ex)
		}
	}

}
