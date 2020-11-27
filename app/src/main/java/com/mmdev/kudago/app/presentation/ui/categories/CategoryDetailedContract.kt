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

package com.mmdev.kudago.app.presentation.ui.categories

import com.mmdev.kudago.app.domain.core.base.DataBaseInfo

/**
 * This is the documentation block about the class
 */

interface CategoryDetailedContract {

	interface View<T: DataBaseInfo> {
		
		fun dataInit(data: List<T>)
		
		fun dataLoadedPrevious(data: List<T>)
		
		fun dataLoadedNext(data: List<T>)
		
		fun hideEmptyListIndicator()
		
		fun showEmptyListIndicator()

		fun showLoading()

		fun hideLoading()

	}

	interface Presenter {

		fun loadFirst(category: String)
		
		fun loadPrevious()
		
		fun loadNext()

	}

}