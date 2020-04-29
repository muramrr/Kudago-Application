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

package com.mmdev.kudago.app.presentation.base

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

/**
 * Base Presenter feature - for Rx Jobs
 *
 * launch() - launch a Rx request
 * clear all request on stop
 */

abstract class BasePresenter<V : IBaseView<P>, out P : IBasePresenter<V>> :
		IBasePresenter<V>,
		CoroutineScope by CoroutineScope(Dispatchers.Main) {

	override lateinit var view: V

	fun launch(job: () -> Unit) {

	}

	@CallSuper
	override fun stop() {
		cancel()
	}


}

interface IBasePresenter<T> {

	fun stop()

	var view: T
}