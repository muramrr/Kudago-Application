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

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.BasePresenter


/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouritesFragment: BaseFragment(R.layout.fragment_favourites) {

	companion object {
		private val ARG_PARAM1 = "param1"
		private val ARG_PARAM2 = "param2"

		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment Favourites.
		 */
		@JvmStatic
		fun newInstance(param1: String, param2: String) = FavouritesFragment()
			.apply {
			arguments = Bundle().apply {
				putString(ARG_PARAM1, param1)
				putString(ARG_PARAM2, param2)
			}
		}
	}


	private var param1: String? = null
	private var param2: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			param1 = it.getString(ARG_PARAM1)
			param2 = it.getString(ARG_PARAM2)
		}
	}

	override val presenter: BasePresenter<*>
		get() = TODO("Not yet implemented")

	override fun setupViews() {
	}

}
