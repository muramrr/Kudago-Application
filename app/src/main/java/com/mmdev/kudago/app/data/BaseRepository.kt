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

import android.util.Log
import com.mmdev.kudago.app.domain.core.ResultState
import retrofit2.Response
import java.io.IOException

/**
 * This is the documentation block about the class
 */

open class BaseRepository {

	suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>,
	                                  errorMessage: String): T? {

		val result : ResultState<T> = safeApiResult(call, errorMessage)
		var data : T? = null

		when(result) {
			is ResultState.Success -> data = result.data
			is ResultState.Error -> {
				Log.d("BaseRepository", "$errorMessage & Exception - ${result.exception}")
			}
		}


		return data

	}

	private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>,
	                                           errorMessage: String) : ResultState<T>{
		val response = call.invoke()
		if(response.isSuccessful) return ResultState.Success(response.body()!!)

		return ResultState.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
	}

	protected fun compareId(id1: Int, id2: Int) : Boolean = id1 == id2
}