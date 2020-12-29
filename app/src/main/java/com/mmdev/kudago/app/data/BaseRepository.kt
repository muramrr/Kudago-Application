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

package com.mmdev.kudago.app.data

import androidx.annotation.WorkerThread
import com.mmdev.kudago.app.core.utils.log.logError
import com.mmdev.kudago.app.core.utils.log.logWarn
import com.mmdev.kudago.app.domain.core.ResultState
import com.mmdev.kudago.app.domain.core.SimpleResult

/**
 * This is the documentation block about the class
 */

@WorkerThread
abstract class BaseRepository {
	
	protected val TAG = "mylogs_${javaClass.simpleName}"
	
	protected inline fun <T> safeCall(TAG: String, call: () -> T): SimpleResult<T> =
		try {
			val result = call.invoke()
			if (result != null) ResultState.success(result)
			else {
				logWarn(TAG, "Safe call returns null")
				ResultState.failure(NullPointerException("Data is null"))
			}
		}
		catch (t: Throwable) {
			logError(TAG, "${t.message}")
			ResultState.Failure(t)
		}
}