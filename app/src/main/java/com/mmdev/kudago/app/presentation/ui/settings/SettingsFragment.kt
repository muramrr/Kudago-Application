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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.core.KudagoApp
import com.mmdev.kudago.app.databinding.FragmentSettingsBinding
import com.mmdev.kudago.app.presentation.base.BaseFragment
import com.mmdev.kudago.app.presentation.ui.common.DialogRate
import com.mmdev.kudago.app.presentation.ui.common.applySystemWindowInsets
import com.mmdev.kudago.app.presentation.ui.common.showSnack
import org.koin.android.ext.android.inject


/**
 * This is the documentation block about the class
 */

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
	R.layout.fragment_settings
), SettingsContract.View {


	override val presenter: SettingsPresenter by inject()

	private lateinit var cityList: Map<String, String>

	private fun getCityList() = mapOf(
		getString(R.string.city_api_ekb) to getString(R.string.city_human_ekb),
		getString(R.string.city_api_krasnoyarsk) to getString(R.string.city_human_krasnoyarsk),
		getString(R.string.city_api_krd) to getString(R.string.city_human_krd),
		getString(R.string.city_api_kzn) to getString(R.string.city_human_kzn),
		getString(R.string.city_api_msk) to getString(R.string.city_human_msk),
		getString(R.string.city_api_nnv) to getString(R.string.city_human_nnv),
		getString(R.string.city_api_nsk) to getString(R.string.city_human_nsk),
		getString(R.string.city_api_spb) to getString(R.string.city_human_spb),
		getString(R.string.city_api_sochi) to getString(R.string.city_human_sochi)
	)
	
	override fun onStart() {
		super.onStart()
		presenter.getCity()
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View = FragmentSettingsBinding.inflate(inflater, container, false).apply {
		_binding = this
	}.root

	override fun setupViews() {
	
//		val imageLoader = ImageLoader.get()
//		val clearCacheString = getString(R.string.settings_btn_clear_cache)
//		val cacheSize = { clearCacheString + imageLoader.getFileCacheSize() + " Mb" }

//		viewBinding.btnClearCache.apply {
//			text = cacheSize.invoke()
//
//			setOnClickListener {
//				imageLoader.clearCache()
//				this.text = cacheSize.invoke()
//			}
//		}
		
		cityList = getCityList()
		viewBinding.layoutSettingsEditCity.applySystemWindowInsets(applyTop = true)
		val adapter = ArrayAdapter(
			requireContext(),
			android.R.layout.simple_dropdown_item_1line,
			cityList.values.toMutableList()
		)
		viewBinding.dropSettingsEditCity.apply {
			setAdapter(adapter)

			setOnItemClickListener { _, _, position, _ ->
				val cityKey = cityList.map { it.key }[position]
				presenter.setCity(cityKey)
			}
		}


		
		viewBinding.btnClearFavourites.setOnClickListener { presenter.clearFavourites() }

		viewBinding.switchDarkTheme.isChecked = presenter.getForceDarkTheme()

		viewBinding.switchDarkTheme.setOnCheckedChangeListener { _, b ->
			presenter.toggleDarkTheme(b)
		}
		
		viewBinding.btnRateApp.setOnClickListener {
			KudagoApp.interactions = 0 //force reset counter
			DialogRate().show(childFragmentManager, DialogRate::class.java.canonicalName)
		}
	}

	

	override fun updateDisplayingCity(city: String) {
		val cityToDisplay = cityList.getValue(city)
		viewBinding.dropSettingsEditCity.setText(cityToDisplay, false)
	}

	override fun setCityIsNotChosen() {
		viewBinding.dropSettingsEditCity.setText(getString(R.string.settings_city_is_not_chosen), false)
	}

	override fun showClearedSnack() = viewBinding.root.showSnack(getString(R.string.successfully_cleared_favourites))
}