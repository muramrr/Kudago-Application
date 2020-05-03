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


import android.util.Log
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.base.BasePresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * This is the documentation block about the class
 */

class PlacesPresenter(val repository: IPlacesRepository) :
		BasePresenter<PlacesContract.View>(),
		PlacesContract.Presenter,
		CoroutineScope by CoroutineScope(Dispatchers.Main) {

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Main + parentJob

	var data: List<PlaceEntity>? = null

	override fun loadPlaces(category: String) {
		launch {
			val result = repository.loadFirstPlaces(category)
			when (result) {
				is ResultState.Success -> {
					data = result.data.results
					//attachedView?.showData(example.user)
				}
				is ResultState.Error -> {
					result.exception.printStackTrace()
				}
			}

			Log.wtf("mylogs", "$data")
		}

	}

	override fun loadMorePlaces() {
		TODO("Not yet implemented")
	}


}