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

package com.mmdev.kudago.app.domain.core

/**
 * Possible results in usecase
 */

sealed class ResultState<out T : Any> {

	/**
	 * A state of [data] which shows that we know there is still an update to come.
	 */
	class Loading<out T: Any>(val data: T) : ResultState<T>()

	/**
	 * A state that shows the [data] is the last known update.
	 */
	class Success<out T : Any>(val data: T) : ResultState<T>()

	/**
	 * A state to show a [Throwable] is thrown.
	 */
	class Error<out T: Any>(val exception: Throwable, val data: T? = null) : ResultState<T>()

}