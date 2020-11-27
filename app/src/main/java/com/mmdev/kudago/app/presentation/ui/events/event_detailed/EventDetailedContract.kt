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

package com.mmdev.kudago.app.presentation.ui.events.event_detailed

import com.mmdev.kudago.app.domain.events.data.EventDetailedInfo
import com.mmdev.kudago.app.presentation.base.mvp.IBasePresenter
import com.mmdev.kudago.app.presentation.base.mvp.IBaseView

/**
 * This is the documentation block about the class
 */

interface EventDetailedContract {


	interface View : IBaseView {

		fun showSuccessDeletedToast()

		fun showSuccessAddedToast()

		fun setRemoveTextFab()

		fun setAddTextFab()

		fun updateData(data: EventDetailedInfo)

		fun setEventDateTime(eventDates: List<EventDateUi>)

	}

	interface Presenter : IBasePresenter<View> {

		fun addOrRemoveEventToFavourites()

		fun loadEventDetailsById(id: Int)

	}

}