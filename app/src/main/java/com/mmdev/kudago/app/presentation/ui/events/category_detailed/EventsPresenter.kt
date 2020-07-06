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

package com.mmdev.kudago.app.presentation.ui.events.category_detailed

import com.mmdev.kudago.app.domain.events.IEventsRepository
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This is the documentation block about the class
 */

class EventsPresenter (private val repository: IEventsRepository) :
		BasePresenter<EventsContract.View>(),
		EventsContract.Presenter {



	override fun loadFirstCategoryEntities(category: String) {
		launch {
			withContext(backgroundContext) { repository.loadFirstEvents(category) }?.let {
				with(it.results) {
					if (isNotEmpty()) getLinkedView()?.setData(this)
					else getLinkedView()?.showEmptyList()
				}

			}
		}
	}

	override fun loadMore() {
		launch {
			withContext(backgroundContext) { repository.loadMoreEvents() }?.let {
				getLinkedView()?.updateData(it.results)
			}

		}
	}


}