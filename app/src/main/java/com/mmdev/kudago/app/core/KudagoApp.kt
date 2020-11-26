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
import android.content.Context
import androidx.core.content.edit
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.mmdev.kudago.app.core.di.DatabaseModule
import com.mmdev.kudago.app.core.di.NetworkModule
import com.mmdev.kudago.app.core.di.PresentersModule
import com.mmdev.kudago.app.core.di.RepositoryModule
import com.mmdev.kudago.app.core.utils.log.DebugConfig
import com.mmdev.kudago.app.core.utils.log.MyLogger
import com.mmdev.kudago.app.core.utils.log.logDebug
import com.mmdev.kudago.app.presentation.ui.common.ThemeHelper
import com.mmdev.kudago.app.presentation.ui.common.ThemeHelper.ThemeMode.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * This is the documentation block about the class
 */

class KudagoApp: Application() {
	
	
	
	companion object {
		private const val TAG = "mylogs_KudagoApp"
		
		private const val PREFERENCES_NAME = "com.mmdev.kudago.app.settings"
		
		private const val DARK_THEME_KEY = "DARK_THEME_KEY"
		private const val CITY_KEY = "CITY_KEY"
		
		private lateinit var appContext: Context
		private val prefs: Preferences by lazy {
			BinaryPreferencesBuilder(appContext).name(PREFERENCES_NAME).build()
		}
		
		
		var darkTheme: Boolean = false
			set(value) {
				field = value
				prefs.edit {
					putBoolean(DARK_THEME_KEY, value)
				}
				logDebug(TAG, "Is dark theme forced? -$value")
				if (value) ThemeHelper.applyTheme(DARK_MODE)
				else ThemeHelper.applyTheme(LIGHT_MODE)
			}
		
		var city: String = ""
		 set(value) {
			 if (field != value) {
			 	field = value
				 prefs.edit {
					 putString(CITY_KEY, value)
				 }
			 }
			
		 }
		
		@Volatile
		var debug: DebugConfig = DebugConfig.Default
		
		/**
		 * Enable or disable [Application] debug mode.
		 * enabled by default
		 *
		 * @param enabled enable the debug mode.
		 * @param logger logging implementation.
		 */
		fun debugMode(enabled: Boolean, logger: MyLogger) {
			debug = object: DebugConfig {
				override val isEnabled = enabled
				override val logger = logger
			}
		}
	}

	override fun onCreate() {
		appContext = this
		
		super.onCreate()
		startKoin {
			androidContext(this@KudagoApp)
			androidLogger()
			modules(
				listOf(
					DatabaseModule,
					PresentersModule,
					RepositoryModule,
					NetworkModule
				)
			)
		}
		
		darkTheme = prefs.getBoolean(DARK_THEME_KEY, false)
		city = prefs.getString(CITY_KEY, "") ?: ""

	}
}