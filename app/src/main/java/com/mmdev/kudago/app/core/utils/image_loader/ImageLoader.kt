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

package com.mmdev.kudago.app.core.utils.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.mmdev.kudago.app.core.utils.CoroutineDispatchers
import com.mmdev.kudago.app.core.utils.image_loader.cache.disk.FileCache
import com.mmdev.kudago.app.core.utils.image_loader.cache.memory.MemoryCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.Collections.synchronizedMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * @param REQUIRED_SIZE depends on image size which will be loaded (lower value - lower image quality)
 * to take off limits of image size, look at [decodeFile] function
 *
 */



class ImageLoader : KoinComponent, CoroutineScope by CoroutineScope(CoroutineDispatchers.mainDispatcher()) {

	override val coroutineContext: CoroutineContext
		get() = CoroutineDispatchers.ioDispatcher()

	private val context: Context by inject()
	companion object {
		private var INSTANCE: ImageLoader? = null

		@Synchronized
		fun get(): ImageLoader {

			return INSTANCE ?: ImageLoader().also {
				INSTANCE = it
				Log.i(TAG, "One Instance initiated and running")
			}

		}



		private const val TAG = "ImageLoader"
		private const val REQUIRED_SIZE = 1000
		private const val CONNECTION_TIMEOUT = 30000 // 30 sec
		private const val READ_TIMEOUT = 30000 // 30 sec
		private const val BUFFER_SIZE = 2048


		fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
			try {
				val bytes = ByteArray(BUFFER_SIZE)
				while (true) {
					val count = inputStream.read(bytes, 0, BUFFER_SIZE)

					if (count == -1) break

					outputStream.write(bytes, 0, count)
				}
			} catch (ex: Exception) { }

		}
	}


	private var memoryCache = MemoryCache.newInstance()
	private var fileCache: FileCache = FileCache(context)
	private val imageViews = synchronizedMap(WeakHashMap<ImageView, String>())
	private var executorService: ExecutorService = Executors.newFixedThreadPool(5)
	private var handler = Handler(Looper.myLooper()!!)

	private var bitmapOptions = BitmapFactory.Options()
	init {
		bitmapOptions.apply { inPreferredConfig = Bitmap.Config.RGB_565 }
	}

	private var bitmapFromMemoryCache: Bitmap? = null
	private var loadedBitmap: Bitmap? = null

	private var scalingValue: Int = 0
	private var widthTMP: Int = 0
	private var heightTMP: Int = 0

	@Synchronized
	fun load(imageView: ImageView, url: String) {
		imageViews[imageView] = url
		bitmapFromMemoryCache = memoryCache.get(url)
		if (bitmapFromMemoryCache != null) imageView.setImageBitmap(bitmapFromMemoryCache)
		else queuePhoto(url, imageView)
	}

	@Synchronized
	fun preload(url: String) {
		if (memoryCache.get(url) != null) return
		else executorService.submit(ImagePreload(url))
	}

	private fun queuePhoto(url: String, imageView: ImageView) {
		val p = ImageToLoad(url, imageView)
		//startLoading(p)
		executorService.submit(PhotosLoader(p))
	}

	private fun getBitmap(url: String): Bitmap? {
		val f = fileCache.getFile(url)

		val b = decodeFile(f)
		if (b != null) return b
		else{
			val imageUrl = URL(url)
			try {

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
				//on error try again
				return getBitmap(url)
			}
		}


	}

	private fun decodeFile(f: File): Bitmap? {
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
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return null
	}

	private fun startLoading(imageToLoad: ImageToLoad) {
		if (imageViewReused(imageToLoad)) return
		launch {
			loadedBitmap = withContext(CoroutineDispatchers.ioDispatcher()){
				getBitmap(imageToLoad.url)
			}
			loadedBitmap?.let { memoryCache.put(imageToLoad.url, it) }


		}
		updateImageView(loadedBitmap, imageToLoad)
	}

	private inner class PhotosLoader(val imageToLoad: ImageToLoad) : Runnable {

		override fun run() {
			try {
				if (imageViewReused(imageToLoad)) return

				loadedBitmap = getBitmap(imageToLoad.url)
				loadedBitmap?.let { memoryCache.put(imageToLoad.url, it) }

				if (imageViewReused(imageToLoad)) return

				handler.post(BitmapDisplayer(loadedBitmap, imageToLoad))
			} catch (th: Throwable) {
				th.printStackTrace()
			}

		}
	}

	private inner class ImagePreload(val imageToLoadUrl: String) : Runnable {

		override fun run() {
			try {
				loadedBitmap = getBitmap(imageToLoadUrl)
				loadedBitmap?.let { memoryCache.put(imageToLoadUrl, it) }

			} catch (th: Throwable) {
				th.printStackTrace()
			}

		}
	}

	private fun imageViewReused(imageToLoad: ImageToLoad): Boolean {
		val tag = imageViews[imageToLoad.imageView]
		return tag == null || tag != imageToLoad.url
	}

	private fun updateImageView(bitmap: Bitmap?, imageToLoad: ImageToLoad) {
		if (imageViewReused(imageToLoad)) return
		bitmap?.run { imageToLoad.imageView.setImageBitmap(this) }
	}

	private inner class BitmapDisplayer(val bitmap: Bitmap?, val imageToLoad: ImageToLoad) :
			Runnable {
		override fun run() {

			updateImageView(bitmap, imageToLoad)

		}
	}

	fun getFileCacheSize() = fileCache.getFileCacheSizeUsed()

	fun clearCache() {
		memoryCache.clear()
		fileCache.clear()
	}

}