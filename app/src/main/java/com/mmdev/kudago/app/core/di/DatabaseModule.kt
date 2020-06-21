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

import android.app.Application
import androidx.room.Room
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.mmdev.kudago.app.data.favourites.db.FavouritesDao
import com.mmdev.kudago.app.data.favourites.db.FavouritesRoomDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * This is the documentation block about the class
 */


private const val DATABASE_NAME = "favourites_db"
private const val PREFERENCES_NAME = "general_prefs"

val DatabaseModule = module {
	//factory { FavouritesRoomDatabase.getDatabase(androidApplication()).getFavouritesDao() }
	single { provideFavouritesDatabase(androidApplication()) }
	single { provideFavouritesDao(db = get()) }
	single { BinaryPreferencesBuilder(androidApplication()).name(PREFERENCES_NAME).build() }
}

private fun provideFavouritesDatabase(app: Application): FavouritesRoomDatabase {
	return Room
		.databaseBuilder(app, FavouritesRoomDatabase::class.java, DATABASE_NAME)
		.fallbackToDestructiveMigration() // get correct db version if schema changed
		.build()
}
//
//
private fun provideFavouritesDao(db: FavouritesRoomDatabase): FavouritesDao = db.getFavouritesDao()