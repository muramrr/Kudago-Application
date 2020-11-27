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

package com.mmdev.kudago.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mmdev.kudago.app.domain.core.base.ImageEntity
import com.mmdev.kudago.app.domain.events.data.EventDetailedInfo
import com.mmdev.kudago.app.domain.places.data.PlaceDetailedInfo

/**
 * This is the documentation block about the class
 */

@Entity(tableName = "favourites")
data class FavouriteEntity(

	@PrimaryKey
	@ColumnInfo(name = "favourite_id")
	val favouriteId: Int,

	@ColumnInfo(name = "favourite_title")
	val favouriteTitle: String,

	@ColumnInfo(name = "favourite_type")
	val favouriteType: String,

	@ColumnInfo(name = "favourite_description")
	val favouriteDescription: String,

	@ColumnInfo(name = "favourite_image")
	val favouriteMainPictureUrl: String) {


	fun mapToPlaceDetailedEntity(): PlaceDetailedInfo {
		return PlaceDetailedInfo(
			id = favouriteId,
			description = favouriteDescription,
			short_title = favouriteTitle,
			images = listOf(ImageEntity(favouriteMainPictureUrl)),
			isAddedToFavourites = true
		)
	}

	fun mapToEventDetailedEntity(): EventDetailedInfo {
		return EventDetailedInfo(
			id = favouriteId,
			description = favouriteDescription,
			short_title = favouriteTitle,
			images = listOf(ImageEntity(favouriteMainPictureUrl)),
			isAddedToFavourites = true
		)
	}

}