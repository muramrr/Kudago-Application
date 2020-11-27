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
import com.mmdev.kudago.app.data.db.FavouriteEntity
import com.mmdev.kudago.app.data.db.FavouritesDao
import com.mmdev.kudago.app.domain.favourites.FavouriteType.*
import com.mmdev.kudago.app.modules.roomTestModule
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
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
	private val favouritesDao: FavouritesDao by inject()

	//another approach without KoinTest
//	private lateinit var favouritesDatabase: FavouritesRoomDatabase //the db instance
//	private lateinit var favouritesDao: FavouritesDao //the dao
//
//	@Before
//	fun setUp() {
//		val context = ApplicationProvider.getApplicationContext<Context>()
//		favouritesDatabase = Room.inMemoryDatabaseBuilder(context, FavouritesRoomDatabase::class.java)
//			.build()
//
//		favouritesDao = favouritesDatabase.habitDao()
//	}

	/**
	 * Override default Koin configuration to use Room in-memory database
	 */
	@Before
	fun before() {
		loadKoinModules(roomTestModule)
	}

	@Test
	fun testInsertPlace() = runBlocking {

		// Create favourite place entity
		val favouriteEntity =
			FavouriteEntity(0, "Title",
			                PLACE.name,
			                "Description", "")

		// Insert entity

		favouritesDao.insertFavourite(favouriteEntity)
		// Request one entity per id
		val requestedEntities = favouritesDao.getFavouritesList(EVENT.name)

		// compare result
		assertEquals(listOf(favouriteEntity), requestedEntities)
		assertTrue(favouritesDao.getFavouritesList(EVENT.name).isEmpty())
	}

	@Test
	fun testInsertEvent() = runBlocking {

		// Create favourite event entity
		val favouriteEntity =
			FavouriteEntity(0, "Title",
			                EVENT.name,
			                "Description", "")

		// Insert entity
		favouritesDao.insertFavourite(favouriteEntity)
		// Request one entity per id
		val requestedEntities = favouritesDao.getFavouritesList(EVENT.name)

		// compare result
		assertEquals(listOf(favouriteEntity), requestedEntities)
		assertTrue(favouritesDao.getFavouritesList(EVENT.name).isEmpty())
	}

	@Test
	fun testDeleteFavourite() = runBlocking {

		// Create casual entity
		val favouriteEntity =
			FavouriteEntity(
				0, "Title",
				EVENT.name,
				"Description",
				""
			)

		// Save entities
		favouritesDao.insertFavourite(favouriteEntity)

		// compare result
		assertEquals(listOf(favouriteEntity), favouritesDao.getFavouritesList(EVENT.name))
		assertTrue(favouritesDao.getFavouritesList(EVENT.name).isEmpty())

		favouritesDao.deleteFavourite(favouriteEntity)


		// compare result
		assertEquals(emptyList<FavouriteEntity>(), favouritesDao.getAllFavourites())
	}

	/**
	 * Close resources
	 */
	@After
	fun after() {
		//favouritesDatabase.close()
		//stopKoin()
	}
}