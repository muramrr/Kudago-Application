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

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File
import java.net.HttpURLConnection

/**
 * This is the documentation block about the class
 */

fun MockWebServer.mockHttpResponse(body: String, responseCode: Int = HttpURLConnection.HTTP_OK) =
	this.enqueue(MockResponse()
		             .setResponseCode(responseCode)
		             .setBody(body)
	)

fun readJson(fileName: String) = File("src/test/java/com/mmdev/kudago/app/res/$fileName").readText()