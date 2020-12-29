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

package com.mmdev.kudago.app.presentation.base.mvp


import com.mmdev.kudago.app.core.KudagoApp
import com.mmdev.kudago.app.core.utils.MyDispatchers
import com.mmdev.kudago.app.core.utils.log.logWtf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Base Presenter feature - for coroutines jobs
 */

abstract class BasePresenter<V: IBaseView> :
		IBasePresenter<V>,
		CoroutineScope by CoroutineScope(MyDispatchers.main()) {
	
	private companion object {
		private const val NUMBER_OF_INTERACTIONS_REQUIRED = 100
	}
	
	protected val TAG = "mylogs_${javaClass.simpleName}"

	private val parentJob: Job = SupervisorJob()

	// By default child coroutines will run on the main thread.
	override val coroutineContext: CoroutineContext
		get() = MyDispatchers.main() + parentJob

	val backgroundContext: CoroutineContext
		get() = MyDispatchers.io()

	protected var attachedView: V? = null

	override fun linkView(view: V) {
		attachedView = view
		
		// check if rate dialog was already interacted by rate 5 stars
		if (KudagoApp.isRateDialogAvailable) {
			
			// logic is simple: if number of saved interactions is bigger or equals to required ->
			// show DialogRate() and reset the saved counter
			KudagoApp.interactions++
			if (KudagoApp.interactions >= NUMBER_OF_INTERACTIONS_REQUIRED) {
				attachedView?.showRateDialog()
				KudagoApp.interactions = 0
			}
		}
		
	}

	override fun unlinkView() {
		attachedView = null
		logWtf(TAG, "unlink view called, attachedView = $attachedView")
		// Parent Job cancels all child coroutines.
		parentJob.cancel()
	}


}
