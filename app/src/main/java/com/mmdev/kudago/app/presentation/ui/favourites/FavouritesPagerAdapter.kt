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

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import com.mmdev.kudago.app.presentation.ui.favourites.favourite_type.FavouritesTypeFragment


/**
 * This is the documentation block about the class
 */

class FavouritesPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {


	override fun createFragment(position: Int) =
		if (position == 0) FavouritesTypeFragment.newInstance(FavouriteType.EVENT.name)
		else FavouritesTypeFragment.newInstance(FavouriteType.PLACE.name)

	override fun getItemCount(): Int = 2

}