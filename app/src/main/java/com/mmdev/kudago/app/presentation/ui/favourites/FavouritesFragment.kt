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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.domain.favourites.FavouriteEntity
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import kotlinx.android.synthetic.main.fragment_favourites.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouritesFragment : BaseFragment(R.layout.fragment_favourites),
                           FavouritesContract.View {


	override val presenter: FavouritesPresenter by inject()

	private val mFavouritesAdapter = FavouritesAdapter()

	companion object {

		private const val CATEGORY_KEY = "CATEGORY"

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)

		arguments?.let {
			//receivedCategoryString = it.getString(CATEGORY_KEY, "")
		}

		presenter.loadFavouritePlaces()

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
				val category = bundleOf(CATEGORY_KEY to item)
				findNavController().navigate(R.id.action_placesCategories_to_placesCategoryDetailed,
				                             category)
			}
		})
	}

	override fun updateData(data: List<FavouriteEntity>) {
		mFavouritesAdapter.setData(data)
	}

}
