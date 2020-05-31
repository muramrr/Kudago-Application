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

package com.mmdev.kudago.app.domain.favourites.db

import androidx.room.*
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity


@Dao
interface FavouritesDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavourite(favouriteEntity: FavouriteEntity)

	@Query(value = "SELECT * FROM favourites WHERE favourite_type = 'place'")
	suspend fun getFavouritePlaces(): List<FavouriteEntity>

	@Query(value = "SELECT * FROM favourites WHERE favourite_type = 'event'")
	suspend fun getFavouriteEvents(): List<FavouriteEntity>

	@Delete
	suspend fun deleteFavourite(favouriteEntity: FavouriteEntity)

}