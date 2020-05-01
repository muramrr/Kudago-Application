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

package com.mmdev.kudago.app.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mmdev.kudago.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
			mainBottomNavigation.menu.getItem(0).setIcon(R.drawable.ic_places_night_24dp)
			mainBottomNavigation.menu.getItem(1).setIcon(R.drawable.ic_events_night_24dp)
			mainBottomNavigation.menu.getItem(2).setIcon(R.drawable.ic_favourites_night_24dp)
			mainBottomNavigation.menu.getItem(3).setIcon(R.drawable.ic_settings_night_24dp)
		}

		val navController = findNavController(R.id.mainFlowFragment)
		mainBottomNavigation.setupWithNavController(navController)

	}



}
