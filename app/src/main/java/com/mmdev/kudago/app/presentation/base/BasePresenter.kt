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


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

/**
 * Base Presenter feature - for coroutines jobs
 *
 */

abstract class BasePresenter<V> : CoroutineScope by CoroutineScope(Dispatchers.Main) {

	protected val parentJob: Job = SupervisorJob()

	// By default child coroutines will run on the main thread.
	override val coroutineContext: CoroutineContext
		get() = Dispatchers.Main + parentJob

	private var attachedView: WeakReference<V>? = null

	protected fun getLinkedView() = attachedView?.get()

	fun linkView(view: V) {
		attachedView = WeakReference(view)
	}

	fun unlinkView(){
		attachedView = null
	}


}
