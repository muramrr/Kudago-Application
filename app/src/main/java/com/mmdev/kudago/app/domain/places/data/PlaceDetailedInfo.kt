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

package com.mmdev.kudago.app.domain.places.data

import com.mmdev.kudago.app.data.db.FavouriteEntity
import com.mmdev.kudago.app.domain.core.base.ImageEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType.PLACE

/**
 * Contains detailed info about place
 */

data class PlaceDetailedInfo(
	val id: Int = 0,
	var title: String = "",
	var short_title: String = "",
	val body_text: String = "",
	val description: String = "",
	val images: List<ImageEntity> = emptyList(),
	val phone: String = "",
	val coords: PlaceCoords = PlaceCoords(),
	var isAddedToFavourites: Boolean = false
) {

	fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(
			favouriteId = id,
			favouriteDescription = description,
			favouriteTitle = short_title,
			favouriteType = PLACE.name,
			favouriteMainPictureUrl = images[0].image
		)
	}

}