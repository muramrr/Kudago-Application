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

package com.mmdev.kudago.app.core.utils.image_loader.common

import android.util.Log
import com.mmdev.kudago.app.core.utils.image_loader.common.MyLogger.Default

/**
 * May be used to create a custom logging solution to override the [Default] behaviour.
 */
interface MyLogger {

	/**
	 * Pass the log details off to the [MyLogger] implementation.
	 *
	 * @param tag used to identify the source of a log message.
	 * @param message the message to be logged.
	 */
	fun log(tag: String, message: String)

	/**
	 * Default implementation of [MyLogger].
	 */
	object Debug : MyLogger {
		override fun log(tag: String, message: String) {
			Log.i(tag, message)
		}
	}

	object Default : MyLogger {
		override fun log(tag: String, message: String) {}
	}
}