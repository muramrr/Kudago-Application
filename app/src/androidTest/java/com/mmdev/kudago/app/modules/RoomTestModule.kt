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

package com.mmdev.kudago.app.modules

import androidx.room.Room
import com.mmdev.kudago.app.domain.favourites.db.FavouritesRoomDatabase
import org.koin.dsl.module

/**
 * This is the documentation block about the class
 */


/**
 * In-Memory Room Database definition
 */
val roomTestModule = module (override = true) {
	single {
		// In-Memory database config
		Room.inMemoryDatabaseBuilder(get(), FavouritesRoomDatabase::class.java)
			.allowMainThreadQueries()
			.build()
	}
}