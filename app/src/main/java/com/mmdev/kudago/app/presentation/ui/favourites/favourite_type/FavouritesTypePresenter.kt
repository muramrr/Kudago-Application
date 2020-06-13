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

package com.mmdev.kudago.app.presentation.ui.favourites.favourite_type

import com.mmdev.kudago.app.data.favourites.db.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.IFavouritesRepository
import com.mmdev.kudago.app.presentation.base.BasePresenter
import kotlinx.coroutines.launch

/**
 * This is the documentation block about the class
 */

class FavouritesTypePresenter (private val repository: IFavouritesRepository) :
		BasePresenter<FavouritesTypeContract.View>(),
		FavouritesTypeContract.Presenter {

	private val placesList: MutableList<FavouriteEntity> = mutableListOf()
	private val eventsList: MutableList<FavouriteEntity> = mutableListOf()


	override fun loadFavouritePlaces () {
		launch {
			placesList.addAll(repository.getFavouritePlaces())
			if (placesList.isNotEmpty()) getLinkedView()?.updateData(placesList)
		}
	}

	override fun loadFavouriteEvents () {
		launch {
			eventsList.addAll(repository.getFavouriteEvents())
			if (eventsList.isNotEmpty()) getLinkedView()?.updateData(eventsList)
		}
	}

	override fun deleteFromFavourites (favouriteEntity: FavouriteEntity) {
		launch {
			repository.deleteFromFavourites(favouriteEntity)
		}
	}

}
