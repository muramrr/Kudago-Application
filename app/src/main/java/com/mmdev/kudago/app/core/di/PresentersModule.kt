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

package com.mmdev.kudago.app.core.di

import com.mmdev.kudago.app.domain.places.IPlacesRepository
import com.mmdev.kudago.app.presentation.ui.places.category_detailed.PlacesPresenter
import org.koin.dsl.module


//presenters
val PresentersModule = module {
	factory {
		providePlacesPresenter(repository = get())
	}
}

private fun providePlacesPresenter(repository: IPlacesRepository): PlacesPresenter {
	return PlacesPresenter(repository)
}