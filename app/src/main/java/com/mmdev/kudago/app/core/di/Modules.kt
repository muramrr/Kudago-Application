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

import com.mmdev.kudago.app.data.api.PlacesApi
import com.mmdev.kudago.app.data.places.PlacesRepositoryImpl
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This is the documentation block about the class
 */

private const val KUDAGO_BASE_URL = "https://kudago.com/public-api/v1.4/"

val applicationModules = module {

	//presenters
//	factory<AbstractActivityPresenter> {
//		ActivityPresenterImpl()
//	}
//
//	factory<AbstractFragmentPresenter> {
//		FragmentPresenterImpl(
//				get(),
//				get()
//		)
//	}

	//repository
//	factory<Repository> { RepoImpl(get()) }

	//database
//	single {
//		Room.databaseBuilder(androidContext(),
//			                 MyDatabase::class.java,
//			                 MyDatabase.DB_NAME).build()
//	}
//	single<PlayerDao> { get<MyDatabase>().playerDao() }

	single {  }

	factory<IPlacesRepository> { PlacesRepositoryImpl(placesApi = get()) }

	//network
	single { provideGson() }
	single { provideRetrofit(gson = get()) }
	factory { providePlacesApi(retrofit = get()) }
}

fun provideGson(): GsonConverterFactory = GsonConverterFactory.create()

fun provideRetrofit(gson: GsonConverterFactory): Retrofit =
	Retrofit.Builder()
		.baseUrl(KUDAGO_BASE_URL)
		.addConverterFactory(gson)
		.build()

fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(PlacesApi::class.java)