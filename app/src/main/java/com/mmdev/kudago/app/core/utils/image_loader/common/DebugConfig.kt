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

package com.mmdev.kudago.app.core.utils.image_loader.common

import com.mmdev.kudago.app.BuildConfig

internal interface DebugConfig {

	val enabled: Boolean
	val logger: MyLogger

	object Default : DebugConfig {
		override val enabled: Boolean = BuildConfig.DEBUG
		override val logger: MyLogger = if (enabled) MyLogger.Debug else MyLogger.Default
	}
}