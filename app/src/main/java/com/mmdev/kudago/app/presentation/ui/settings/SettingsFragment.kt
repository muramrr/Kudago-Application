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

import android.widget.ArrayAdapter
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.databinding.FragmentSettingsBinding
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.base.viewBinding
import com.mmdev.kudago.app.presentation.ui.common.ThemeHelper
import com.mmdev.kudago.app.presentation.ui.common.ThemeHelper.ThemeMode
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.image_loader.ImageLoader
import org.koin.android.ext.android.inject


/**
 * This is the documentation block about the class
 */

class SettingsFragment : BaseFragment(R.layout.fragment_settings),
                         SettingsContract.View {

	private val viewBinding by viewBinding(FragmentSettingsBinding::bind)

	override val presenter: SettingsPresenter by inject()

	private val cityList = mapOf(
			"ekb" to "Екатеринбург",
			"krasnoyarsk" to "Красноярск",
			"krd" to "Краснодар",
			"kzn" to "Казань",
			"msk" to "Москва",
			"nnv" to "Нижний Новгород",
			"nsk" to "Новосибирск",
			"spb" to "Санкт-Петербург",
			"sochi" to "Сочи"
	)

	override fun setupViews() {
		val imageLoader = ImageLoader(requireContext())
		val cacheSize = { "Clear Cache\n ${imageLoader.getFileCacheSize()} Mb" }

		viewBinding.layoutSettingsEditCity.applySystemWindowInsets(applyTop = true)
		val adapter = ArrayAdapter(requireContext(),
		                           android.R.layout.simple_dropdown_item_1line,
		                           cityList.values.toMutableList())
		viewBinding.dropSettingsEditCity.apply {
			setAdapter(adapter)

			setOnItemClickListener { _, _, position, _ ->
				val cityKey = cityList.map { it.key }[position]
				presenter.setCity(cityKey)
			}
		}

		viewBinding.btnClearCache.apply {
			text = cacheSize.invoke()

			setOnClickListener {
				imageLoader.clearCache()
				this.text = cacheSize.invoke()
			}
		}

		viewBinding.btnClearFavourites.setOnClickListener { presenter.clearFavourites() }

		viewBinding.switchDarkTheme.isChecked = presenter.getForceDarkTheme()

		viewBinding.switchDarkTheme.setOnCheckedChangeListener { compoundButton, b ->
			presenter.setForceDarkTheme(b)
		}
	}

	override fun onStart() {
		super.onStart()
		presenter.getCity()
	}

	override fun updateSettings(city: String) {
		val cityToDisplay = cityList.getValue(city)
		viewBinding.dropSettingsEditCity.setText(cityToDisplay, false)
	}

	override fun updateTheme(themeMode: ThemeMode) {
		ThemeHelper.applyTheme(themeMode)
		navController.navigateUp()
	}
}