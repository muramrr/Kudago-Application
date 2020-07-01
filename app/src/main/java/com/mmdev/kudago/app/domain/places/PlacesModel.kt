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

import com.mmdev.kudago.app.data.favourites.db.FavouriteEntity
import com.mmdev.kudago.app.data.favourites.db.FavouriteType
import com.mmdev.kudago.app.data.favourites.db.IMapperFavourite
import com.mmdev.kudago.app.domain.core.ImageEntity

/**
 * Places entities
 * Places response - list of PlaceItem received from api call
 */

data class PlaceEntity (val id: Int = 0,
                        val title: String = "",
                        val short_title: String = "",
                        val images: List<ImageEntity> = emptyList())

data class PlacesResponse (val results: List<PlaceEntity> = emptyList())

data class PlaceDetailedEntity (val id: Int = 0,
                                var title: String = "",
                                var short_title: String = "",
                                val body_text: String = "",
                                val description: String = "",
                                val images: List<ImageEntity> = emptyList(),
                                val phone: String = "",
                                var isAddedToFavourites: Boolean = false):

		IMapperFavourite {

	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(
				favouriteId = id,
				favouriteDescription = description,
				favouriteTitle = short_title,
				favouriteType = FavouriteType.PLACE.name,
				favouriteMainPictureUrl = images[0].image)
	}

}

interface IMapperPlace {
	fun mapToPlaceDetailedEntity(): PlaceDetailedEntity
}