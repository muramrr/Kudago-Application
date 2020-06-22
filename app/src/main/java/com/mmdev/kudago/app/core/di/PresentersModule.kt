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


import com.mmdev.kudago.app.presentation.ui.events.category_detailed.EventsPresenter
import com.mmdev.kudago.app.presentation.ui.events.event_detailed.EventDetailedPresenter
import com.mmdev.kudago.app.presentation.ui.favourites.favourite_type.FavouritesTypePresenter
import com.mmdev.kudago.app.presentation.ui.places.category_detailed.PlacesPresenter
import com.mmdev.kudago.app.presentation.ui.places.place_detailed.PlaceDetailedPresenter
import com.mmdev.kudago.app.presentation.ui.settings.SettingsPresenter
import org.koin.dsl.module


//presenters
val PresentersModule = module {

	factory { PlacesPresenter(repository = get()) }
	factory { PlaceDetailedPresenter(repository = get()) }

	factory { EventsPresenter(repository = get()) }
	factory { EventDetailedPresenter(repository = get()) }

	factory { FavouritesTypePresenter(repository = get()) }

	factory { SettingsPresenter(settingsWrapper = get()) }

}