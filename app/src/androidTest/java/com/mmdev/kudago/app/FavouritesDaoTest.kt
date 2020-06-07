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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import com.mmdev.kudago.app.domain.favourites.db.FavouritesDao
import com.mmdev.kudago.app.domain.favourites.db.FavouritesRoomDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * WeatherDAOTest is a KoinTest with AndroidJUnit4 runner
 *
 * KoinTest help inject Koin components from actual runtime
 */

@RunWith(AndroidJUnit4::class)
class FavouritesDaoTest : KoinTest {

	/*
	 * Inject needed components from Koin
	 */
	private val favouritesDatabase: FavouritesRoomDatabase by inject()
	private val favouritesDao: FavouritesDao by inject()


	/**
	 * Override default Koin configuration to use Room in-memory database
	 */
	@Before
	fun before() {
		loadKoinModules(roomTestModule)
	}

	@Test
	fun testInsert() = runBlocking {

		// Create weather entities from location and date
		val favouriteEntity = FavouriteEntity(
		                                      favouriteTitle = "Title",
		                                      favouriteType = FavouriteType.PLACE.name,
		                                      favouriteDescription = "Description")

		// Save entities

		favouritesDao.insertFavourite(favouriteEntity)
		// Request one entity per id
		val requestedEntities = favouritesDao.getFavouritePlaces()

		// compare result
		Assert.assertEquals(listOf(favouriteEntity), requestedEntities)
	}

	/**
	 * Close resources
	 */
	@After
	fun after() {
		favouritesDatabase.close()
		stopKoin()
	}
}