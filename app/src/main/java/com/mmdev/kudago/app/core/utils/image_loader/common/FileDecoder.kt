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


	fun decodeFile(f: File): Bitmap? {
		try {

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


		} catch (e: FileNotFoundException) {
			logDebug(TAG, "Decoding error: ${e.localizedMessage}")
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return null
	}

}