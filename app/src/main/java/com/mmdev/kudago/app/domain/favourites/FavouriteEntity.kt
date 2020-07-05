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

package com.mmdev.kudago.app.domain.favourites

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mmdev.kudago.app.domain.core.entity.ImageEntity
import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.IMapperEvent
import com.mmdev.kudago.app.domain.places.IMapperPlace
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import kotlinx.android.parcel.Parcelize

/**
 * This is the documentation block about the class
 */

@Entity(tableName = "favourites")
@Parcelize
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
	val favouriteMainPictureUrl: String): Parcelable, IMapperEvent, IMapperPlace {


	override fun mapToPlaceDetailedEntity(): PlaceDetailedEntity {
		return PlaceDetailedEntity(id = favouriteId,
		                           description = favouriteDescription,
		                           short_title = favouriteTitle,
		                           images = listOf(ImageEntity(favouriteMainPictureUrl)),
		                           isAddedToFavourites = true)
	}

	override fun mapToEventDetailedEntity(): EventDetailedEntity {
		return EventDetailedEntity(id = favouriteId,
		                           description = favouriteDescription,
		                           short_title = favouriteTitle,
		                           images = listOf(ImageEntity(favouriteMainPictureUrl)),
		                           isAddedToFavourites = true)
	}

}


enum class FavouriteType { EVENT, PLACE}