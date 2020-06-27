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
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.mmdev.kudago.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(R.layout.activity_main) {

	override fun onCreate(savedInstanceState: Bundle?) {

		window.apply {
			clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			decorView.systemUiVisibility =
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
						View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			//status bar and navigation bar colors assigned in style file
		}

		super.onCreate(savedInstanceState)


		//set night icons
		if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
			mainBottomNavigation.menu.getItem(0).setIcon(R.drawable.ic_bottom_nav_places_night_24dp)
			mainBottomNavigation.menu.getItem(1).setIcon(R.drawable.ic_bottom_nav_events_night_24dp)
			mainBottomNavigation.menu.getItem(2).setIcon(R.drawable.ic_bottom_nav_favourites_night_24dp)
			mainBottomNavigation.menu.getItem(3).setIcon(R.drawable.ic_bottom_nav_settings_night_24dp)
		}

		val navController = findNavController(R.id.mainFlowFragment)
		//mainBottomNavigation.setupWithNavController(navController)

		//launchSingleTop doesn't work, so here is solution
		mainBottomNavigation.setOnNavigationItemSelectedListener {
			val previousItem = mainBottomNavigation.selectedItemId
			val nextItem = it.itemId

			if (previousItem != nextItem) {

				when (nextItem) {
					R.id.mainBottomNavPlaces -> {
						navController.popBackStack()
						navController.navigate(R.id.mainBottomNavPlaces)
					}
					R.id.mainBottomNavEvents -> {
						navController.popBackStack()
						navController.navigate(R.id.mainBottomNavEvents)
					}
					R.id.mainBottomNavFavourites -> {
						navController.popBackStack()
						navController.navigate(R.id.mainBottomNavFavourites)

					}
					R.id.mainBottomNavSettings -> {
						navController.popBackStack()
						navController.navigate(R.id.mainBottomNavSettings)
					}
				}
			}

			return@setOnNavigationItemSelectedListener true
		}

	}



}
