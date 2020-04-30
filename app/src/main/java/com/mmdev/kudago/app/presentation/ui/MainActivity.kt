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
import com.mmdev.kudago.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {


		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		//not working?
//		val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFlowFragment) as NavHostFragment
//		val navController = navHostFragment.findNavController()
//		mainBottomNavigation.setupWithNavController(navController)

		val navController = findNavController(R.id.mainFlowFragment)

		mainBottomNavigation.setOnNavigationItemSelectedListener {
			val previousItem = mainBottomNavigation.selectedItemId
			val nextItem = it.itemId

			if (previousItem != nextItem) {

				when (nextItem) {
					R.id.mainBottomNavPlaces -> {
						navController.popBackStack()
						navController.navigate(R.id.action_global_placesFragment)

					}
					R.id.mainBottomNavEvents -> {
						navController.popBackStack()
						navController.navigate(R.id.eventsFragment)

					}
					R.id.mainBottomNavFavourites -> {
						navController.popBackStack()
						navController.navigate(R.id.favouritesFragment)

					}
					R.id.mainBottomNavSettings -> {
						navController.popBackStack()
						navController.navigate(R.id.settingsFragment)

					}
				}
			}

			return@setOnNavigationItemSelectedListener true
		}
	}



}
