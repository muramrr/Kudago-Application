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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * FileDecoder
 * @param REQUIRED_SIZE depends on image size which will be loaded (lower value - lower image quality)
 * to take off limits of image size, look at [decodeFile] function
 */

class FileDecoder {

	companion object {
		private const val TAG = "FileDecoder"

		// depends on image size which will be loaded (lower value - lower image quality)
		private const val REQUIRED_SIZE = 900
	}


	private var bitmapOptions = BitmapFactory.Options()
	init {
		bitmapOptions.apply { inPreferredConfig = Bitmap.Config.RGB_565 }
	}
	private var scalingValue: Int = 0
	private var widthTMP: Int = 0
	private var heightTMP: Int = 0

	private var decodedBitmap : Bitmap? = null


	fun decodeFile(f: File): Bitmap? {
		var tempStream: FileInputStream? = null
		var finalStream: FileInputStream? = null
		try {

			// First decode with inJustDecodeBounds=true to check dimensions
			bitmapOptions.inJustDecodeBounds = true

			tempStream = FileInputStream(f)
			BitmapFactory.decodeStream(tempStream, null, bitmapOptions)
				.also { tempStream.close() }


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

			finalStream = FileInputStream(f)
			decodedBitmap = BitmapFactory.decodeStream(finalStream, null, bitmapOptions)
			finalStream.close()

			return decodedBitmap


		} catch (e: FileNotFoundException) {
			logDebug(TAG, "Decoding error: ${e.localizedMessage}")
			e.printStackTrace()
			return null
		} catch (e: IOException) {
			e.printStackTrace()
			return null
		} finally {
			tempStream?.close()
			finalStream?.close()
		}

	}

}