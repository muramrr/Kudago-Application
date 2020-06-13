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

package com.mmdev.kudago.app.presentation.ui.common.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.Collections.synchronizedMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @param [REQUIRED_SIZE] depends on image size which will be loaded (lower value - lower image quality)
 * to take off limits of image size, look at [decodeFile] function
 *
 */



class ImageLoader(context: Context) {

	companion object {
		private var INSTANCE: ImageLoader? = null

		@Synchronized
		fun with(context: Context): ImageLoader {

			return INSTANCE ?: ImageLoader(context).also {
				INSTANCE = it
				Log.i(TAG, "One Instance initiated and running")
			}

		}

		private const val TAG = "ImageLoader"
		private const val REQUIRED_SIZE = 1400
		private const val CONNECTION_TIMEOUT = 30000
		private const val READ_TIMEOUT = 30000
		private const val BUFFER_SIZE = 2048


		fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
			try {
				val bytes = ByteArray(BUFFER_SIZE)
				while (true) {
					val count = inputStream.read(bytes, 0, BUFFER_SIZE)

					if (count == -1) break

					outputStream.write(bytes, 0, count)
				}
			} catch (ex: Exception) {
			}

		}
	}

	private var memoryCache = MemoryCache()
	private var fileCache: FileCache = FileCache(context)
	private val imageViews = synchronizedMap(WeakHashMap<ImageView, String>())
	private var executorService: ExecutorService = Executors.newFixedThreadPool(5)
	private var handler = Handler(Looper.myLooper()!!)

	@Synchronized
	fun load(imageView: ImageView, url: String) {
		imageViews[imageView] = url
		val bitmap = memoryCache[url]
		if (bitmap != null) imageView.setImageBitmap(bitmap)
		else queuePhoto(url, imageView)
	}

	private fun queuePhoto(url: String, imageView: ImageView) {
		val p = PhotoToLoad(url, imageView)
		executorService.submit(PhotosLoader(p))
	}

	private fun getBitmap(url: String): Bitmap? {
		val f = fileCache.getFile(url)

		val b = decodeFile(f)
		if (b != null) return b

		try {
			val imageUrl = URL(url)
			val conn = imageUrl.openConnection() as HttpURLConnection
			conn.apply {
				connectTimeout = CONNECTION_TIMEOUT
				instanceFollowRedirects = true
				readTimeout = READ_TIMEOUT
			}
			val outStream = FileOutputStream(f)
			copyStream(conn.inputStream, outStream)
			outStream.close()
			conn.disconnect()
			return decodeFile(f)
		} catch (ex: Throwable) {
			ex.printStackTrace()
			if (ex is OutOfMemoryError) memoryCache.clear()
			return null
		}

	}

	private fun decodeFile(f: File): Bitmap? {
		try {
			val o = BitmapFactory.Options()
			o.inJustDecodeBounds = true
			if (f.exists()) {
				val stream1 = FileInputStream(f)
				BitmapFactory.decodeStream(stream1, null, o)
				stream1.close()

				var widthTMP = o.outWidth
				var heightTMP = o.outHeight
				var scale = 1
				while (true) {
					if (widthTMP / 2 < REQUIRED_SIZE || heightTMP / 2 < REQUIRED_SIZE) break

					widthTMP /= 2
					heightTMP /= 2
					scale *= 2
				}

				val bitmapOptions = BitmapFactory.Options()
				bitmapOptions.inSampleSize = scale
				val stream2 = FileInputStream(f)
				val bitmap = BitmapFactory.decodeStream(stream2, null, bitmapOptions)
				stream2.close()
				return bitmap
			}
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return null
	}

	private data class PhotoToLoad(val url: String, val imageView: ImageView)

	private inner class PhotosLoader(val photoToLoad: PhotoToLoad) : Runnable {

		override fun run() {
			try {
				if (imageViewReused(photoToLoad)) return

				val bmp = getBitmap(photoToLoad.url)
				memoryCache.put(photoToLoad.url, bmp!!)

				if (imageViewReused(photoToLoad)) return

				val bd = BitmapDisplayer(bmp, photoToLoad)
				handler.post(bd)
			} catch (th: Throwable) {
				th.printStackTrace()
			}

		}
	}

	private fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
		val tag = imageViews[photoToLoad.imageView]
		return tag == null || tag != photoToLoad.url
	}

	private inner class BitmapDisplayer(val bitmap: Bitmap?, val photoToLoad: PhotoToLoad) :
			Runnable {
		override fun run() {
			if (imageViewReused(photoToLoad)) return

			bitmap?.let { photoToLoad.imageView.setImageBitmap(it) }

		}
	}

	fun clearCache() {
		memoryCache.clear()
		fileCache.clear()
	}

}