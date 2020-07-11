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
import android.graphics.BitmapFactory
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * FileCache
 * @param REQUIRED_SIZE depends on image size which will be loaded (lower value - lower image quality)
 * to take off limits of image size, look at [decodeFile] function
 */

class FileCache(context: Context) {

	companion object {
		private const val TAG = "DiskCache"

		// depends on image size which will be loaded (lower value - lower image quality)
		private const val REQUIRED_SIZE = 1000
	}


	private var bitmapOptions = BitmapFactory.Options()
	init {
		bitmapOptions.apply { inPreferredConfig = Bitmap.Config.RGB_565 }
	}
	private var scalingValue: Int = 0
	private var widthTMP: Int = 0
	private var heightTMP: Int = 0




	private var cacheDir: File? = null
	init {
		cacheDir = context.cacheDir
		if (!cacheDir!!.exists()) cacheDir!!.mkdirs()
	}

	fun getFile(url: String): File {

		val f = File(cacheDir, url.hashCode().toString())
		if (f.exists()) {
			logDebug(TAG, "Get from disk cache: ${url.md5()}")
		}
		return f

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
		logDebug(TAG, "Clear memory: ${getFileCacheSizeUsed()} mb")
	}

	fun decodeFile(f: File): Bitmap? {
		try {

			if (f.exists()) {
				// First decode with inJustDecodeBounds=true to check dimensions
				bitmapOptions.inJustDecodeBounds = true

				val fileInputStream = FileInputStream(f)
				BitmapFactory.decodeStream(fileInputStream, null, bitmapOptions)
					.also { fileInputStream.close() }


				widthTMP = bitmapOptions.outWidth
				heightTMP = bitmapOptions.outHeight
				scalingValue = 1
				while (true) {
					if (widthTMP / 2 < REQUIRED_SIZE || heightTMP / 2 < REQUIRED_SIZE) break

					widthTMP /= 2
					heightTMP /= 2
					scalingValue *= 2
				}

				bitmapOptions.inSampleSize = scalingValue
				bitmapOptions.inJustDecodeBounds = false

				val stream2 = FileInputStream(f)
				val bitmap = BitmapFactory.decodeStream(stream2, null, bitmapOptions)
				stream2.close()

				return bitmap

			}
		} catch (e: FileNotFoundException) {
			logDebug(TAG, "Decoding error: ${e.localizedMessage}")
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return null
	}
}