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
import android.graphics.Bitmap
import com.mmdev.kudago.app.core.utils.image_loader.cache.bytesToMegabytes
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.common.FileDecoder
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.File


class FileCache(context: Context) {

	companion object {
		private const val TAG = "DiskCache"

	}


	private val fileDecoder = FileDecoder()
	private var cacheDir: File? = null
	init {
		cacheDir = context.cacheDir
		if (!cacheDir!!.exists()) cacheDir!!.mkdirs()
	}

	private var bitmapFromCache: Bitmap? = null

	fun getFile(url: String): File = File(cacheDir, url.hashCode().toString())

	fun getBitmapFromDiskCache(url: String) : Bitmap? {
		val f = getFile(url)
		return if (f.exists()) {

			bitmapFromCache = fileDecoder.decodeFile(f)
			logDebug(TAG, "Get from disk cache: ${url.md5()}, result = $bitmapFromCache")
			bitmapFromCache
		}
		else {
			bitmapFromCache?.recycle()
			null
		}
	}

	fun getFileCacheSizeUsed(): Int {
		var size: Long = 0
		val files = cacheDir!!.listFiles() ?: return 0
		for (f in files) {
			size += f.length()
		}
		return size.bytesToMegabytes()
	}

	fun clear() {
		val files = cacheDir!!.listFiles() ?: return
		for (f in files) f.delete()
		logDebug(TAG, "Clear memory: ${getFileCacheSizeUsed()} mb")
	}


}