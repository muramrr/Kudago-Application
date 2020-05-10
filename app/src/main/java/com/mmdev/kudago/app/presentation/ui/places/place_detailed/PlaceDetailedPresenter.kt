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
import com.mmdev.kudago.app.presentation.base.BasePresenter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * This is the documentation block about the class
 */

class PlaceDetailedPresenter (private val repository: IPlacesRepository):
		BasePresenter<PlaceDetailedContract.View>(),
		PlaceDetailedContract.Presenter,
		CoroutineScope by CoroutineScope(Dispatchers.Main) {


	private val parentJob = SupervisorJob()

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Main + parentJob



	override fun loadPlaceDetailsById(id: Int) {
		launch {
			val result = withContext(coroutineContext) {
				repository.getPlaceDetails(id)
			}
			when (result) {
				is ResultState.Success -> {
					getLinkedView()?.updateData(result.data.images.map { it.image })
				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

		}
	}
}