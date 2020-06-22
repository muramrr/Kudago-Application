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

package com.mmdev.kudago.app.presentation.ui.settings

import android.os.Bundle
import android.widget.ArrayAdapter
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentSettingsBinding
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.showToast
import org.koin.android.ext.android.inject


/**
 * This is the documentation block about the class
 */

class SettingsFragment : BaseFragment(R.layout.fragment_settings),
                         SettingsContract.View {

	private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

	override val presenter: SettingsPresenter by inject()

	private val cityList = mapOf(
			"Екатеринбург" to "ekb",
			"Красноярск" to "krasnoyarsk",
			"Краснодар" to "krd",
			"Казань" to "kzn",
			"Москва" to "msk",
			"Нижний Новгород" to "nnv",
			"Новосибирск" to "nsk",
			"Санкт-Петербург" to "spb",
			"Сочи" to "sochi"
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		presenter.linkView(this)
	}

	override fun setupViews() {
		viewBinding.toolbarSettings.applySystemWindowInsets(applyTop = true)
		val adapter = ArrayAdapter(requireContext(),
		                           android.R.layout.simple_dropdown_item_1line,
		                           cityList.keys.toMutableList())
		viewBinding.dropSettingsEditCity.setAdapter(adapter)

		viewBinding.dropSettingsEditCity.setOnItemClickListener { _, _, position, _ ->
			val city = cityList.map { it.value }[position]
			presenter.setCity(city)
		}
	}

	override fun onStart() {
		super.onStart()
		presenter.getCity()
	}

	override fun showToast(toastText: String) = requireContext().showToast(toastText)

	override fun updateSettings(city: String) {
		viewBinding.dropSettingsEditCity.setText(city)
	}
}