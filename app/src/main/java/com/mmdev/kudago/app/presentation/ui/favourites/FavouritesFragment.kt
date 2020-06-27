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

package com.mmdev.kudago.app.presentation.ui.favourites

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentFavouritesBinding
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets

/**
 * This is the documentation block about the class
 */

class FavouritesFragment : BaseFragment(R.layout.fragment_favourites){


	private val viewBinding by viewBinding(FragmentFavouritesBinding::bind)


	override fun setupViews() {
		viewBinding.tabLayoutContainer.applySystemWindowInsets(applyTop = true)

		viewBinding.viewPagerContainer.apply {
			adapter = FavouritesPagerAdapter(childFragmentManager, lifecycle)
		}

		TabLayoutMediator(viewBinding.tabLayoutContainer,
		                  viewBinding.viewPagerContainer) { tab: TabLayout.Tab, position: Int ->
			when (position){
				0 -> tab.text = getString(R.string.favourite_tab_events)
				1 -> tab.text = getString(R.string.favourite_tab_places)
			}
		}.attach()
	}

}