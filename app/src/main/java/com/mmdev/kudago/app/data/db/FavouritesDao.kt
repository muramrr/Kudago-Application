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

package com.mmdev.kudago.app.data.db

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavouritesDao {

	@VisibleForTesting
	@Query("SELECT * FROM favourites")
	suspend fun getAllFavourites(): List<FavouriteEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFavourite(favouriteEntity: FavouriteEntity)

	@Query("SELECT * FROM favourites WHERE favourite_type = :type")
	suspend fun getFavouritesList(type: String): List<FavouriteEntity>

	@Query("SELECT * FROM favourites WHERE favourite_type = :type AND favourite_id = :id")
	suspend fun getFavouriteById(type: String, id: Int): FavouriteEntity?

	@Query("DELETE FROM favourites WHERE favourite_id = :id")
	suspend fun deleteFavourite(id: Int)

	@Query("DELETE FROM favourites")
	suspend fun deleteAll()

}