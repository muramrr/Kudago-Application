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

import com.mmdev.kudago.app.core.KudagoApp
import com.mmdev.kudago.app.core.utils.log.logError
import com.mmdev.kudago.app.core.utils.log.logInfo
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
	
	override fun loadFirst(category: String) {
		launch {
			attachedView?.showLoading()
			withContext(backgroundContext) {
				repository.loadFirstEvents(KudagoApp.city, category)
			}.fold(
				success = {
					attachedView?.dataInit(it.results)
					
					if (it.results.isNotEmpty()) attachedView?.hideEmptyListIndicator()
					else attachedView?.showEmptyListIndicator()
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
			attachedView?.hideLoading()
		}
	}
	
	override fun loadPrevious() {
		logInfo(TAG, "invoked to load previous events")
		launch {
			withContext(backgroundContext) {
				repository.loadPreviousEvents()
			}.fold(
				success = {
					attachedView?.dataLoadedPrevious(it.results)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}
	
	override fun loadNext() {
		logInfo(TAG, "invoked to load next events")
		launch {
			withContext(backgroundContext) {
				repository.loadNextEvents()
			}.fold(
				success = {
					attachedView?.dataLoadedNext(it.results)
				},
				failure = {
					logError(TAG, "${it.message}")
				}
			)
		}
	}

}