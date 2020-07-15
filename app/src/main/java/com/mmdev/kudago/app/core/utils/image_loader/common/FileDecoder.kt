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
import com.mmdev.kudago.app.core.utils.image_loader.cache.memory.BitmapPool
import com.mmdev.kudago.app.core.utils.image_loader.common.FileDecoder.decodeAndScaleFile
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * FileDecoder
 * @param REQUIRED_SIZE depends on image size which will be loaded (lower value - lower image quality)
 * to take off limits of image size, look at [decodeAndScaleFile] function
 */
object FileDecoder {

	private const val TAG = "FileDecoder"

	// depends on image size which will be loaded (lower value - lower image quality)
	private const val REQUIRED_SIZE = 500
	private const val REQUIRED_HEIGHT = 500
	private const val REQUIRED_WIDTH = 500

	fun decodeAndScaleFile(f: File, bitmapPool: BitmapPool,
	                       reqWidth: Int = REQUIRED_WIDTH,
	                       reqHeight: Int = REQUIRED_HEIGHT): Bitmap? {

		return try {
			val filePath = f.path
			val bitmapOptions = BitmapFactory.Options()
			// First decode with inJustDecodeBounds=true to check dimensions
			bitmapOptions.inJustDecodeBounds = true
			BitmapFactory.decodeFile(filePath, bitmapOptions)

			// Calculate inSampleSize
			bitmapOptions.inSampleSize =
				if (reqHeight > 0 && reqWidth > 0) calculateInSampleSize(bitmapOptions, reqWidth, reqHeight)
				else 1

			addInBitmapOptions(bitmapOptions, bitmapPool)

			bitmapOptions.inJustDecodeBounds = false
			BitmapFactory.decodeFile(filePath, bitmapOptions)

		} catch (e: FileNotFoundException) {
			logDebug(TAG, "Decoding error: ${e.localizedMessage}")
			e.printStackTrace()
			null
		} catch (e: IOException) {
			e.printStackTrace()
			null
		}

	}

	private fun addInBitmapOptions(options: BitmapFactory.Options, bitmapPool: BitmapPool) {
		// inBitmap only works with mutable bitmaps, so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true
		// Try to find a bitmap to use for inBitmap.
		bitmapPool.getReusableBitmap(options)?.let { inBitmap ->
			// If a suitable bitmap has been found, set it as the value of inBitmap.
			options.inBitmap = inBitmap
		}
	}

	private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
		// Raw height and width of image
		val height = options.outHeight
		val width = options.outWidth
		var inSampleSize = 1
		if (height > reqHeight || width > reqWidth) {
			val halfHeight = height / 2
			val halfWidth = width / 2

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
				inSampleSize *= 2
			}
		}
		return inSampleSize
	}


}