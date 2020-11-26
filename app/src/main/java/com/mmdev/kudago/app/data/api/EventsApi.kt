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

package com.mmdev.kudago.app.data.api

import com.mmdev.kudago.app.domain.events.EventDetailedEntity
import com.mmdev.kudago.app.domain.events.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit events api calls
 */

interface EventsApi {

	@GET("events/?fields=id,title,short_title,images&text_format=plain")
	suspend fun getEventsList(
		@Query("actual_since") timestamp: Long,
		@Query("categories") category: String,
		@Query("location") location: String,
		@Query("page") page: Int = 1
	): EventsResponse

	@GET("events/{id}/?fields=id,title,short_title," +
	     "body_text,description,images,dates,price,is_free")
	suspend fun getEventDetails(@Path("id") id: Int): EventDetailedEntity

}