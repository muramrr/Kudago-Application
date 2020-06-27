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

import com.mmdev.kudago.app.domain.places.PlaceDetailedEntity
import com.mmdev.kudago.app.presentation.base.IBasePresenter
import com.mmdev.kudago.app.presentation.base.IBaseView


/**
 * This is the documentation block about the class
 */

interface PlaceDetailedContract {


	interface View : IBaseView {

		fun showSuccessDeletedToast()

		fun showSuccessAddedToast()

		fun setRemoveTextFab()

		fun setAddTextFab()

		fun updateData(data: PlaceDetailedEntity)

	}

	interface Presenter : IBasePresenter<View> {

		fun addOrRemovePlaceToFavourites()

		fun loadPlaceDetailsById(id: Int)

	}

}