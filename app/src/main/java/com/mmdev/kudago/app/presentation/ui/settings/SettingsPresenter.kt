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

import com.mmdev.kudago.app.data.settings.SettingsImpl
import com.mmdev.kudago.app.presentation.base.BasePresenter

/**
 * This is the documentation block about the class
 */

class SettingsPresenter (private val settingsWrapper: SettingsImpl) :
		BasePresenter<SettingsContract.View>(),
		SettingsContract.Presenter {

	override fun getCity() {
		settingsWrapper.readCity().run {
			if (this.isNotBlank()) getLinkedView()?.updateSettings(this)
			else getLinkedView()?.updateSettings("jopa")
		}
	}

	override fun setCity(city: String) {
		settingsWrapper.changeCity(city)
	}

}