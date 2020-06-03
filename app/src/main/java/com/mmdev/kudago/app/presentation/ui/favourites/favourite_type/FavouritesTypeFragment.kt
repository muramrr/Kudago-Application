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

package com.mmdev.kudago.app.presentation.ui.favourites.favourite_type

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import kotlinx.android.synthetic.main.fragment_favourites_type_list.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouritesTypeFragment : BaseFragment(R.layout.fragment_favourites_type_list),
                               FavouritesTypeContract.View {


	override val presenter: FavouritesTypePresenter by inject()

	private val mFavouritesAdapter = FavouritesTypeAdapter()

	companion object {

		private const val FAVOURITE_TYPE_KEY = "FAVOURITE_TYPE"
		private const val EVENT_ID_KEY = "EVENT_ID"
		private const val PLACE_ID_KEY = "PLACE_ID"

		fun newInstance(category: String) = FavouritesTypeFragment().apply {
			arguments = Bundle().apply {
				putString(FAVOURITE_TYPE_KEY, category)
			}
		}

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			when(it.getString(FAVOURITE_TYPE_KEY, "")) {
				FavouriteType.EVENT.name -> presenter.loadFavouriteEvents()
				FavouriteType.PLACE.name -> presenter.loadFavouritePlaces()
			}
		}

	}

	override fun setupViews() {
		rvFavouritesListTest.applySystemWindowInsets(applyTop = true)
		rvFavouritesListTest.apply {
			adapter = mFavouritesAdapter
			layoutManager = LinearLayoutManager(this.context)
		}

		mFavouritesAdapter.setOnItemClickListener(object : BaseAdapter
		                                                   .OnItemClickListener<FavouriteEntity> {

			override fun onItemClick(item: FavouriteEntity, position: Int) {
				if (item.favouriteType == FavouriteType.EVENT.name) {
					val id = bundleOf(EVENT_ID_KEY to item.id)
					navController.navigate(R.id.action_favourites_to_eventDetailed, id)
				}
				else {
					val id = bundleOf(PLACE_ID_KEY to item.id)
					navController.navigate(R.id.action_favourites_to_placeDetailed, id)
				}


			}
		})
	}

	override fun updateData(data: List<FavouriteEntity>) {
		mFavouritesAdapter.setData(data)
	}

}
