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

package com.mmdev.kudago.app.data.favourites.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity


/**
 * This is the documentation block about the class
 */

@Database(entities = [FavouriteEntity::class], version = 1, exportSchema = false)
abstract class FavouritesRoomDatabase: RoomDatabase() {

	abstract fun getFavouritesDao(): FavouritesDao


	//todo: needs refractoring
	companion object {

		private const val DATABASE_NAME = "favourites_db"

		@Volatile private var INSTANCE: FavouritesRoomDatabase? = null

		fun getDatabase(app: Application): FavouritesRoomDatabase =
			INSTANCE
			?: synchronized(this) {
				INSTANCE
				?: buildDatabase(
						app)
					.also { INSTANCE = it }
			}

		private fun buildDatabase(app: Application) =
			Room.databaseBuilder(app, FavouritesRoomDatabase::class.java,
			                     DATABASE_NAME)
				.fallbackToDestructiveMigration()
				.build()
	}


}