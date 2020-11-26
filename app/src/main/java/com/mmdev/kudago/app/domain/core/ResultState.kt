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
 * Primary used to define bounds between network and ui states
 */

typealias SimpleResult<T> = ResultState<T, Throwable>

sealed class ResultState<out T, out E> {
	
	data class Success<out T>(val data: T) :  ResultState<T, Nothing>()
	
	data class Failure<out E>(val error: E) : ResultState<Nothing, E>()
	
	companion object {
		fun <T> success(data: T) = Success(data)
		fun <E> failure(error: E) = Failure(error)
	}
	
	inline fun <C> fold(success: (T) -> C,
	                    failure: (E) -> C): C =
		when (this) {
			is Success -> success(data)
			is Failure -> failure(error)
		}
	
}
