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

package com.mmdev.kudago.app.presentation.ui.places.place_detailed

import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.presentation.base.BasePresenter
import com.mmdev.kudago.app.presentation.ui.common.capitalizeRu
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This is the documentation block about the class
 */

class PlaceDetailedPresenter (private val repository: IPlacesRepository):
		BasePresenter<PlaceDetailedContract.View>(),
		PlaceDetailedContract.Presenter {

	private lateinit var placeDetailedEntity: PlaceDetailedEntity

	private var isAdded = false

	override fun addOrRemovePlaceToFavourites() {
		launch {
			val result = withContext(coroutineContext) {
				if (isAdded) repository.removePlaceFromFavouritesList(placeDetailedEntity)
				else repository.addPlaceToFavouritesList(placeDetailedEntity)
			}
			when (result) {
				is ResultState.Success -> {
					isAdded = if (isAdded){
						getLinkedView()?.showToast("Successfully removed from favourites")
						false
					}
					else {
						getLinkedView()?.showToast("Successfully added to favourites")
						true
					}
					handleFabState(isAdded)

				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

		}
	}

	@ExperimentalStdlibApi
	override fun loadPlaceDetailsById(id: Int) {
		launch {
			val result = withContext(coroutineContext) {
				repository.getPlaceDetails(id)
			}
			when (result) {
				is ResultState.Success -> {
					placeDetailedEntity = result.data
					placeDetailedEntity.short_title.capitalizeRu()
					getLinkedView()?.updateData(placeDetailedEntity)
					handleFabState(placeDetailedEntity.isAddedToFavourites)

					isAdded = placeDetailedEntity.isAddedToFavourites
				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

		}
	}

	private fun handleFabState(added: Boolean) {
		if (added) getLinkedView()?.updateFabButton("Remove from favourites")
		else getLinkedView()?.updateFabButton("Add to favourites")
	}
}