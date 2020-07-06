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

package com.mmdev.kudago.app.presentation.ui.base

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentCategoriesListBinding
import com.mmdev.kudago.app.presentation.base.BaseAdapter
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets

/**
 * This is the documentation block about the class
 */

abstract class CategoriesFragment : BaseFragment(R.layout.fragment_categories_list) {

	private val viewBinding by viewBinding(FragmentCategoriesListBinding::bind)

	protected val CATEGORY_KEY = "CATEGORY"
	protected val TITLE_KEY = "TITLE"


	override fun setupViews() {
		viewBinding.rvCategories.applySystemWindowInsets(applyTop = true)

		val eventsCategoriesAdapter = CategoriesAdapter(setupAdapterList())
		viewBinding.rvCategories.apply {
			adapter = eventsCategoriesAdapter
			layoutManager = LinearLayoutManager(this.context)
			addItemDecoration(DividerItemDecoration(this.context, RecyclerView.VERTICAL))
			setHasFixedSize(true)
		}

		eventsCategoriesAdapter.setOnItemClickListener(adapterItemClick())
	}

	abstract fun setupAdapterList(): List<CategoriesAdapter.AdapterCategoryItem>

	abstract fun adapterItemClick() : BaseAdapter.OnItemClickListener<CategoriesAdapter.AdapterCategoryItem>

}