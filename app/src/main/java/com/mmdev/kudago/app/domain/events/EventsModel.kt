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

import com.mmdev.kudago.app.domain.core.entity.BaseDetailedEntity
import com.mmdev.kudago.app.domain.core.entity.BaseEntity
import com.mmdev.kudago.app.domain.core.entity.ImageEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Events entities
 * Events response - list of EventItem received from api call
 */

data class EventEntity (
	override val id: Int = 0,
	override val title: String = "",
	override val short_title: String = "",
	override val images: List<ImageEntity> = emptyList()
) : BaseEntity

data class EventsResponse (val results: List<EventEntity> = emptyList())

data class EventDetailedEntity (
	override val id: Int = 0,
	override var title: String = "",
	override var short_title: String = "",
	override val body_text: String = "",
    override val description: String = "",
    override val images: List<ImageEntity> = emptyList(),
    val dates: List<EventDate> = emptyList(),
    val price: String = "",
    val is_free: Boolean = false,
    override var isAddedToFavourites: Boolean = false
): BaseDetailedEntity {


	fun mapToUIEventDateList(): List<UIEventDate> {
		return dates
				//filter dates which end value bigger than current time
			.filter{ eventDate -> eventDate.end > System.currentTimeMillis() / 1000L }
				//map filtered items
			.map{ eventDate -> eventDate.mapToUIEventDate() }
	}

	override fun mapToFavourite(): FavouriteEntity {
		return FavouriteEntity(
			favouriteId = id,
			favouriteDescription = description,
			favouriteTitle = short_title,
			favouriteType = FavouriteType.EVENT.name,
			favouriteMainPictureUrl = images[0].image
		)
	}

}

//event date object according to fit api specs
data class EventDate(val start: Long = 0, val end: Long = 0) {

	fun mapToUIEventDate() : UIEventDate {

		val timeFormatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
		val dateFormatter = SimpleDateFormat("EEE, dd MMMM, yyyy", Locale.getDefault())

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
			                                              timeFormatter.format(fEnd),
			                                              fStart.time, fEnd.time)

			//end is undefined
			fStart != null && fEnd == null -> UIEventDate(startDate = dateFormatter.format(fStart),
			                                              startTime = timeFormatter.format(fStart),
			                                              startInMillis = fStart.time)

			//start & end is undefined
			else -> UIEventDate()
		}

	}
}

//event date which we want to see in ui
data class UIEventDate(
	val startDate: String = "",
	val startTime: String = "",
	val endDate: String? = null,
	val endTime: String? = null,
	val startInMillis: Long = 0,
	val endInMillis: Long = 0) {

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

