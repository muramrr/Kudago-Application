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

package com.mmdev.kudago.app.domain.events

import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import com.mmdev.kudago.app.domain.favourites.IMapperFavourite

/**
 * Events entities
 * Events response - list of EventItem received from api call
 */

data class EventEntity (val id: Int = 0,
                        val title: String = "",
                        val short_title: String = "",
                        val images: List<ImageEntity> = emptyList())

data class EventsResponse (val results: List<EventEntity> = emptyList())

data class EventDetailedEntity (val id: Int = 0,
                                val title: String = "",
                                val short_title: String = "",
                                val body_text: String = "",
                                val description: String = "",
                                val images: List<ImageEntity> = emptyList(),
                                var isAddedToFavourites: Boolean = false):

		IMapperFavourite {

	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(favouriteId = id,
		                       favouriteDescription = description,
		                       favouriteTitle = title,
		                       favouriteType = FavouriteType.EVENT.name)
	}

}

//image url
//do not change because it is api declaration
data class ImageEntity (val image: String = "")

interface IMapperEvent {
	fun mapToEventDetailedEntity(): EventDetailedEntity
}

