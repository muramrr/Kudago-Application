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
import java.text.SimpleDateFormat
import java.util.*

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
                                val dates: List<EventDate> = emptyList(),
                                val price: String = "",
                                val is_free: Boolean = false,
                                var isAddedToFavourites: Boolean = false): IMapperFavourite {

	data class EventDate(val start: Long = 0, val end: Long = 0) {

		fun mapToUIEventDate() : UIEventDate {

			val timeFormatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
			val dateFormatter = SimpleDateFormat("EEE, dd MMMM, YYYY", Locale.getDefault())

			//check if start is defined correctly
			var fStart: Date? = null
			if (start != -62135433000) //03.01.0001
				fStart = Date(start * 1000L)

			//check if end is defined correctly
			var fEnd: Date? = null
			if (end != 253370754000) //01.01.9999
				fEnd = Date(end * 1000L)

			return when {
				//both are properly defined
				fStart != null && fEnd != null -> UIEventDate(dateFormatter.format(fStart),
				                                              timeFormatter.format(fStart),
				                                              dateFormatter.format(fEnd),
				                                              timeFormatter.format(fEnd))

				//end is undefined
				fStart != null && fEnd == null -> UIEventDate(dateFormatter.format(fStart),
				                                              timeFormatter.format(fStart))

				//start & end is undefined
				else -> UIEventDate()
			}

		}
	}

	fun mapToUIEventDateList(): List<UIEventDate> {
		return dates
			.filter{ eventDate -> eventDate.end > System.currentTimeMillis() / 1000L }
			.map{ eventDate -> eventDate.mapToUIEventDate() }
	}

	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(
				favouriteId = id,
				favouriteDescription = description,
				favouriteTitle = short_title,
				favouriteType = FavouriteType.EVENT.name,
				favouriteMainPictureUrl = images[0].image)
	}

}

data class UIEventDate(val startDate: String = "", val startTime: String = "",
                       val endDate: String? = null, val endTime: String? = null) {

	fun getDate(): String {
		if (endDate != null )
			if (startDate != endDate) return "$startDate - $endDate"
		return startDate
	}

	fun getTime(): String {
		if (endDate != null)
			if (startTime != endTime) return "$startTime - $endTime"
		return startTime
	}
}


interface IMapperEvent {
	fun mapToEventDetailedEntity(): EventDetailedEntity
}

