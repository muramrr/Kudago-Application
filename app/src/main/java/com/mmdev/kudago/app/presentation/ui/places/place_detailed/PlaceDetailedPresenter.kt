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

import com.mmdev.kudago.app.core.utils.log.logError
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.data.PlaceDetailedInfo
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This is the documentation block about the class
 */

class PlaceDetailedPresenter(private val repository: IPlacesRepository):
		BasePresenter<PlaceDetailedContract.View>(),
		PlaceDetailedContract.Presenter {

	private lateinit var placeDetailedInfo: PlaceDetailedInfo

	private var isAdded = false

	override fun addOrRemovePlaceToFavourites() {
		launch {
			withContext(coroutineContext) {
				if (isAdded) repository.removePlaceFromFavouritesList(placeDetailedInfo)
				else repository.addPlaceToFavouritesList(placeDetailedInfo)
			}.fold(
				success = {
					isAdded = if (isAdded){
						attachedView?.showSuccessDeletedToast()
						false
					}
					else {
						attachedView?.showSuccessAddedToast()
						true
					}
					handleFabState(isAdded)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)

		}
	}

	override fun loadPlaceDetailsById(id: Int) {
		launch {
			withContext(coroutineContext) {
				repository.getPlaceDetails(id)
			}.fold(
				success = {
					placeDetailedInfo = it
					attachedView?.updateData(it)
					attachedView?.setMarkerOnMap(
						it.coords,
						it.short_title
					)
					
					handleFabState(it.isAddedToFavourites)
					
					isAdded = it.isAddedToFavourites
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}

	private fun handleFabState(added: Boolean) {
		if (added) attachedView?.setRemoveTextFab()
		else attachedView?.setAddTextFab()
	}
}