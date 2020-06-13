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

package com.mmdev.kudago.app

import com.mmdev.kudago.app.data.api.PlacesApi
import com.mmdev.kudago.app.data.places.PlacesRepositoryImpl
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.data.favourites.db.FavouritesDao
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.ImageEntity
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * This is the documentation block about the class
 */

@RunWith(JUnit4::class)
class ResultStateTest {

	private val mockWebServer: MockWebServer = MockWebServer()
	private lateinit var repository: IPlacesRepository

	private val expectedEntity = PlaceDetailedEntity(
			id = 12271,
			title = "School number 235",
			description = "Some description text\n\n",
			body_text = "Some body text",
			images = listOf(
					ImageEntity(image = "https://kudago" +
					                    ".com/media/images/place/a0/5b/a05b17ab412c8958d8ae1421a78b9b0e.jpg"),
					ImageEntity(image = "https://kudago" +
					                    ".com/media/images/place/ef/21/ef21dd125d3877d338f7ef5983d10fd5.bmp"),
					ImageEntity(image = "https://kudago" +
					                    ".com/media/images/place/55/29/5529ea7dada771077e560609025f604a.bmp")
			),
			short_title = "School #235"
	)

	private val expectedResultState = ResultState.Success(expectedEntity)


	@Before
	fun before() {
		val favouritesDao = mock(FavouritesDao::class.java)

		val retrofit = Retrofit.Builder()
			//.baseUrl("https://kudago.com/public-api/v1.4/")
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		val placesApi = retrofit.create(PlacesApi::class.java)

		repository = PlacesRepositoryImpl(placesApi, favouritesDao)
	}

	@Test
	fun testSuccessResultState() {
		val body = readJson("place_by_id_response.json")
		mockWebServer.mockHttpResponse(body, HttpURLConnection.HTTP_OK)


		val placeDetailedResponse = ResultState.Success(expectedEntity)

		runBlocking {
			val repositoryResultState = repository.getPlaceDetails(12271)

			when(repositoryResultState){
				//same data
				is ResultState.Success -> assertEquals(expectedResultState.data, repositoryResultState.data)
			}

			//not comparable
			assertNotEquals(placeDetailedResponse, repositoryResultState)
			//same states
			assertEquals(placeDetailedResponse.getState(), repositoryResultState.getState())

		}
	}

	@Test
	fun testErrorResultState() {

		mockWebServer.mockHttpResponse("no result", HttpURLConnection.HTTP_NOT_FOUND)

		runBlocking {
			val repositoryResultState = repository.getPlaceDetails(0)


			when(repositoryResultState){
				//same data
				is ResultState.Success -> assertNotEquals(expectedResultState.data, repositoryResultState.data)
				//same error
				is ResultState.Error -> assertEquals(repositoryResultState.exception.message,
				                                     "HTTP ${HttpURLConnection.HTTP_NOT_FOUND} " +
				                                     "Client Error")
			}

			//not comparable
			assertNotEquals(expectedResultState, repositoryResultState)
			//same states
			assertTrue { repositoryResultState.getState().name == "Error" }

		}
	}


}