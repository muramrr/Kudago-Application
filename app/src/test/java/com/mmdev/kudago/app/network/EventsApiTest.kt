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

package com.mmdev.kudago.app.network

import com.mmdev.kudago.app.data.api.EventsApi
import com.mmdev.kudago.app.mockHttpResponse
import com.mmdev.kudago.app.readJson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * You have to ask yourself though, what exactly is it that you're trying to test?
 * There's no need to test Retrofit's functionality.
 * These tests best serve the purpose of validating that the data models you have created
 * for API responses are indeed correct,
 * and that your parser can correctly handle unexpected values (such as null) gracefully.
 * It is therefore important that the sample responses that you pick are actual responses from your API,
 * so that your data models can be validated against real data in tests before being used in the app.
 */

@RunWith(JUnit4::class)
class EventsApiTest {

	private lateinit var mockWebServer: MockWebServer
	private lateinit var retrofit: Retrofit
	private lateinit var service: EventsApi

	@Before
	fun before() {
		mockWebServer = MockWebServer()
		retrofit = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(GsonConverterFactory.create())
			.build()
		service = retrofit.create(EventsApi::class.java)
	}



	@Test
	fun apiTest() {
		val body = readJson("events_response.json")

		runBlocking {

			mockWebServer.mockHttpResponse(body)

			val response = service.getEventsListAsync(1, "", "")

			assertNotEquals(0, response.body()?.results?.size)
			assertTrue(response.body()!!.results[0].id == 155978)
		}
	}

	@After
	fun after() {
		mockWebServer.close()
	}
}