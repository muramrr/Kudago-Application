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

package com.mmdev.kudago.app.core.utils.log

import android.util.Log
import com.mmdev.kudago.app.core.utils.log.MyLogger.Default

/**
 * May be used to create a custom logging solution to override the [Default] behaviour.
 */

interface MyLogger {

	/**
	 * @param tag used to identify the source of a log message.
	 * @param message the message to be logged.
	 */
	fun warn(tag: String, message: String)

	fun error(tag: String, message: String)

	fun debug(tag: String, message: String)

	fun info(tag: String, message: String)

	fun wtf(tag: String, message: String)

	/**
	 * Debug implementation of [MyLogger].
	 */
	object Debug: MyLogger {

		override fun warn(tag: String, message: String) {
			Log.w(tag, message)
		}

		override fun error(tag: String, message: String) {
			Log.e(tag, message)
		}

		override fun debug(tag: String, message: String) {
			Log.d(tag, message)
		}

		override fun info(tag: String, message: String) {
			Log.i(tag, message)
		}

		override fun wtf(tag: String, message: String) {
			Log.wtf(tag, message)
		}
	}

	/**
	 * Default implementation of [MyLogger].
	 * No messages to Logcat
	 */
	object Default : MyLogger {
		override fun warn(tag: String, message: String) {}
		override fun error(tag: String, message: String) {}
		override fun debug(tag: String, message: String) {}
		override fun info(tag: String, message: String) {}
		override fun wtf(tag: String, message: String) {}
	}
}