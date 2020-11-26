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

package com.mmdev.kudago.app.data.repository

import com.mmdev.kudago.app.data.BaseRepository
import com.mmdev.kudago.app.data.db.FavouritesDao
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.IFavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * another approach coroutines getters
 */

class FavouritesRepositoryImpl(
	private val favouritesDao: FavouritesDao
): BaseRepository(), IFavouritesRepository {

	
	override suspend fun addToFavourite(favouriteEntity: FavouriteEntity) =
		withContext(Dispatchers.IO) {
			favouritesDao.insertFavourite(favouriteEntity)
		}

	
	override suspend fun deleteFromFavourites(favouriteEntity: FavouriteEntity) =
		withContext(Dispatchers.IO) {
			favouritesDao.deleteFavourite(favouriteEntity)
		}

	
	override suspend fun getFavouritePlaces(): List<FavouriteEntity> =
		withContext(Dispatchers.IO) {
			favouritesDao.getFavouritePlaces()
		}

	
	override suspend fun getFavouriteEvents(): List<FavouriteEntity> =
		withContext(Dispatchers.IO) {
			favouritesDao.getFavouriteEvents()
		}
}