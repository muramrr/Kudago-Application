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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.data.db.FavouriteEntity
import com.mmdev.kudago.app.databinding.FragmentFavouritesTypeListBinding
import com.mmdev.kudago.app.domain.favourites.FavouriteType
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.custom.LinearItemDecoration
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouritesTypeFragment : BaseFragment<FragmentFavouritesTypeListBinding>(
	R.layout.fragment_favourites_type_list
), FavouritesTypeContract.View {


	override val presenter: FavouritesTypePresenter by inject()

	private val mAdapter = FavouritesTypeAdapter()

	private var receivedFavouriteType = ""

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

		arguments?.let {
			receivedFavouriteType = it.getString(FAVOURITE_TYPE_KEY, "")
		}

	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View = FragmentFavouritesTypeListBinding.inflate(inflater, container, false).apply {
		_binding = this
	}.root

	override fun onStart() {
		super.onStart()
		when(receivedFavouriteType) {
			FavouriteType.EVENT.name -> presenter.loadFavouriteEvents()
			FavouriteType.PLACE.name -> presenter.loadFavouritePlaces()
		}
	}

	override fun setupViews() {

		viewBinding.rvFavouritesList.apply {
			adapter = mAdapter
			layoutManager = LinearLayoutManager(this.context)
			addItemDecoration(LinearItemDecoration())
		}

		mAdapter.setOnItemClickListener { view, position, item ->
			if (item.favouriteType == FavouriteType.EVENT.name) {
				val id = bundleOf(EVENT_ID_KEY to item.favouriteId)
				navController.navigate(R.id.action_favourites_to_eventDetailed, id)
			}
			else {
				val id = bundleOf(PLACE_ID_KEY to item.favouriteId)
				navController.navigate(R.id.action_favourites_to_placeDetailed, id)
			}
		}
	}

	override fun updateData(data: List<FavouriteEntity>) {
		mAdapter.updateData(data)
		viewBinding.rvFavouritesList.visibility = View.VISIBLE
		viewBinding.tvEmptyFavourites.visibility = View.INVISIBLE
	}

	override fun showEmptyFavourites() {
		viewBinding.tvEmptyFavourites.visibility = View.VISIBLE
		viewBinding.rvFavouritesList.visibility = View.INVISIBLE
	}
	
	override fun onDestroyView() {
		viewBinding.rvFavouritesList.adapter = null
		super.onDestroyView()
	}

}
