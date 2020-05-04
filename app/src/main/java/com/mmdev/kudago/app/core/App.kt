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
import androidx.appcompat.app.AppCompatDelegate
import com.mmdev.kudago.app.core.di.NetworkModule
import com.mmdev.kudago.app.core.di.PresentersModule
import com.mmdev.kudago.app.core.di.RepositoryModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.random.Random

/**
 * This is the documentation block about the class
 */

class App: Application() {

	private val applicationModules = listOf(PresentersModule,
	                                        RepositoryModules,
	                                        NetworkModule)

	override fun onCreate() {

//		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

		super.onCreate()
		startKoin {
			androidContext(this@App)
			androidLogger()
			modules(applicationModules)
		}

		// Random nightMode to aware of day/night themes
		val nightMode = when (Random.nextBoolean()) {
			true -> AppCompatDelegate.MODE_NIGHT_YES
			false -> AppCompatDelegate.MODE_NIGHT_NO
		}
		AppCompatDelegate.setDefaultNightMode(nightMode)
	}
}