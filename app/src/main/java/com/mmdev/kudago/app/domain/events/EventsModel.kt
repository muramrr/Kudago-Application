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

import com.mmdev.kudago.app.data.favourites.db.FavouriteEntity
import com.mmdev.kudago.app.data.favourites.db.FavouriteType
import com.mmdev.kudago.app.data.favourites.db.IMapperFavourite
import com.mmdev.kudago.app.domain.core.ImageEntity

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
                                var title: String = "",
                                var short_title: String = "",
                                val body_text: String = "",
                                val description: String = "",
                                val images: List<ImageEntity> = emptyList(),
                                val dates: Array<EventDate> = emptyArray(),
                                val price: String = "",
                                val is_free: Boolean = false,
                                var isAddedToFavourites: Boolean = false): IMapperFavourite {

	data class EventDate(val start: Long = 0, val end: Long = 0)


	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(
				favouriteId = id,
				favouriteDescription = description,
				favouriteTitle = short_title,
				favouriteType = FavouriteType.EVENT.name,
				favouriteMainPictureUrl = images[0].image)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as EventDetailedEntity

		if (id != other.id) return false
		if (title != other.title) return false
		if (short_title != other.short_title) return false
		if (body_text != other.body_text) return false
		if (description != other.description) return false
		if (images != other.images) return false
		if (!dates.contentEquals(other.dates)) return false
		if (price != other.price) return false
		if (is_free != other.is_free) return false
		if (isAddedToFavourites != other.isAddedToFavourites) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id
		result = 31 * result + title.hashCode()
		result = 31 * result + short_title.hashCode()
		result = 31 * result + body_text.hashCode()
		result = 31 * result + description.hashCode()
		result = 31 * result + images.hashCode()
		result = 31 * result + dates.contentHashCode()
		result = 31 * result + price.hashCode()
		result = 31 * result + is_free.hashCode()
		result = 31 * result + isAddedToFavourites.hashCode()
		return result
	}

}

interface IMapperEvent {
	fun mapToEventDetailedEntity(): EventDetailedEntity
}

