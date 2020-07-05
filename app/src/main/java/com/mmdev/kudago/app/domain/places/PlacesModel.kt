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

import com.mmdev.kudago.app.domain.core.entity.BaseDetailedEntity
import com.mmdev.kudago.app.domain.core.entity.BaseEntity
import com.mmdev.kudago.app.domain.core.entity.ImageEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType

/**
 * Places entities
 * Places response - list of PlaceItem received from api call
 */

data class PlaceEntity (override val id: Int = 0,
                        override val title: String = "",
                        override val short_title: String = "",
                        override val images: List<ImageEntity> = emptyList()) : BaseEntity

data class PlacesResponse (val results: List<PlaceEntity> = emptyList())

data class PlaceDetailedEntity (override val id: Int = 0,
                                override var title: String = "",
                                override var short_title: String = "",
                                override  val body_text: String = "",
                                override val description: String = "",
                                override val images: List<ImageEntity> = emptyList(),
                                val phone: String = "",
                                override var isAddedToFavourites: Boolean = false): BaseDetailedEntity {

	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(favouriteId = id,
		                       favouriteDescription = description,
		                       favouriteTitle = short_title,
		                       favouriteType = FavouriteType.PLACE.name,
		                       favouriteMainPictureUrl = images[0].image)
	}

}

interface IMapperPlace {
	fun mapToPlaceDetailedEntity(): PlaceDetailedEntity
}