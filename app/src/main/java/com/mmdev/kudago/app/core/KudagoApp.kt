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

package com.mmdev.kudago.app.core

import android.app.Application
import com.ironz.binaryprefs.Preferences
import com.mmdev.kudago.app.core.di.DatabaseModule
import com.mmdev.kudago.app.core.di.NetworkModule
import com.mmdev.kudago.app.core.di.PresentersModule
import com.mmdev.kudago.app.core.di.RepositoryModule
import com.mmdev.kudago.app.presentation.ui.common.ThemeHelper
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * This is the documentation block about the class
 */

class KudagoApp: Application() {

	private val applicationModules = listOf(DatabaseModule,
	                                        PresentersModule,
	                                        RepositoryModule,
	                                        NetworkModule)

	override fun onCreate() {


		super.onCreate()
		startKoin {
			androidContext(this@KudagoApp)
			androidLogger()
			modules(applicationModules)
		}
		val prefs: Preferences by inject()
		val darkMode = prefs.getBoolean("DARK_THEME_KEY", false)
		if (darkMode) ThemeHelper.applyTheme(ThemeHelper.ThemeMode.DARK_MODE)
		// Random nightMode to aware of day/night themes
//		val nightMode = when (Random.nextBoolean()) {
//			true -> AppCompatDelegate.MODE_NIGHT_YES
//			false -> AppCompatDelegate.MODE_NIGHT_NO
//		}
//		AppCompatDelegate.setDefaultNightMode(nightMode)
	}
}