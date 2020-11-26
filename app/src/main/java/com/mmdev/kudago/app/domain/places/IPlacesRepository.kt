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

package com.mmdev.kudago.app.domain.places

import com.mmdev.kudago.app.domain.core.SimpleResult

/**
 * Places commands interface
 */

interface IPlacesRepository {

	suspend fun addPlaceToFavouritesList(placeDetailedEntity: PlaceDetailedEntity): SimpleResult<Unit>

	suspend fun loadFirstPlaces(city: String, category: String): SimpleResult<PlacesResponse>
	
	suspend fun loadPreviousPlaces(): SimpleResult<PlacesResponse>
	
	suspend fun loadNextPlaces(): SimpleResult<PlacesResponse>

	suspend fun getPlaceDetails(id: Int): SimpleResult<PlaceDetailedEntity>

	suspend fun removePlaceFromFavouritesList(placeDetailedEntity: PlaceDetailedEntity): SimpleResult<Unit>

}