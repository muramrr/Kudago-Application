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

package com.mmdev.kudago.app.presentation.ui.places.category_detailed


import com.mmdev.kudago.app.core.KudagoApp
import com.mmdev.kudago.app.core.utils.log.logError
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * This is the documentation block about the class
 */

class PlacesPresenter(private val repository: IPlacesRepository) :
		BasePresenter<PlacesContract.View>(),
		PlacesContract.Presenter {

	override fun loadFirst(category: String) {
		launch {
			withContext(backgroundContext) {
				repository.loadFirstPlaces(KudagoApp.city, category)
			}.fold(
				success = {
					getLinkedView()?.dataInit(it.results)
					
					if (it.results.isNotEmpty()) getLinkedView()?.hideEmptyListIndicator()
					else getLinkedView()?.showEmptyListIndicator()
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}
	
	override fun loadPrevious() {
		launch {
			withContext(backgroundContext) {
				repository.loadPreviousPlaces()
			}.fold(
				success = {
					getLinkedView()?.dataLoadedPrevious(it.results)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}
	
	override fun loadNext() {
		launch {
			withContext(backgroundContext) {
				repository.loadNextPlaces()
			}.fold(
				success = {
					getLinkedView()?.dataLoadedNext(it.results)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}
	
}