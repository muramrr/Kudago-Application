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

package com.mmdev.kudago.app.core.utils.image_loader.cache.disk

import android.content.Context
import java.io.File

/**
 * FileCache
 */

class FileCache(context: Context) {

	private var cacheDir: File? = null

	init {
		cacheDir = context.cacheDir
		if (!cacheDir!!.exists()) cacheDir!!.mkdirs()
	}

	fun getFile(url: String): File {
		val filename = url.hashCode().toString()
		return File(cacheDir, filename)

	}

	fun getFileCacheSizeUsed(): Long {
		var size: Long = 0
		val files = cacheDir!!.listFiles() ?: return 0L
		for (f in files) {
			size += f.length()
		}
		return size/1024/1024
	}

	fun clear() {
		val files = cacheDir!!.listFiles() ?: return
		for (f in files) f.delete()
	}
}