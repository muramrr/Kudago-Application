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

package com.mmdev.kudago.app.core.di

import com.mmdev.kudago.app.core.KudagoApp
import com.mmdev.kudago.app.data.api.EventsApi
import com.mmdev.kudago.app.data.api.PlacesApi
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val KUDAGO_BASE_URL = "https://kudago.com/public-api/v1.4/"

//network
val NetworkModule = module {
	single { provideRetrofit() }
	single { provideEventsApi(retrofit = get()) }
	single { providePlacesApi(retrofit = get()) }
}

private fun provideRetrofit(): Retrofit = Retrofit.Builder().apply {
	baseUrl(KUDAGO_BASE_URL)
	addConverterFactory(GsonConverterFactory.create())
	if (KudagoApp.debug.isEnabled) client(okHttpClient)
}.build()

private fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create(EventsApi::class.java)
private fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(PlacesApi::class.java)

private val okHttpClient: OkHttpClient
	get() = OkHttpClient.Builder()
		.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
		.addInterceptor(baseInterceptor)
		.build()

private val baseInterceptor: Interceptor = invoke { chain ->
	val newUrl = chain
		.request()
		.url
		.newBuilder()
		.build()
	
	val request = chain
		.request()
		.newBuilder()
		.url(newUrl)
		.build()
	
	return@invoke chain.proceed(request)
}