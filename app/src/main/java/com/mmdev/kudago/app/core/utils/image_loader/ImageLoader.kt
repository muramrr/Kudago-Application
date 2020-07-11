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
import android.widget.ImageView
import com.mmdev.kudago.app.core.utils.MyDispatchers
import com.mmdev.kudago.app.core.utils.image_loader.cache.disk.FileCache
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.cache.memory.MemoryCache
import com.mmdev.kudago.app.core.utils.image_loader.common.DebugConfig
import com.mmdev.kudago.app.core.utils.image_loader.common.MyLogger
import com.mmdev.kudago.app.core.utils.image_loader.network.BitmapDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import java.util.Collections.synchronizedMap
import kotlin.coroutines.CoroutineContext


class ImageLoader : KoinComponent, CoroutineScope {

	override val coroutineContext: CoroutineContext
		get() = Dispatchers.IO


	private val context: Context by inject()

	companion object {
		private const val TAG = "ImageLoader"

		@Volatile
		internal var debug: DebugConfig = DebugConfig.Default

		/**
		 * Enable or disable [ImageLoader] debug mode.
		 *
		 * @param enabled enable the debug mode.
		 * @param logger logging implementation.
		 */
		fun debugMode(enabled: Boolean, logger: MyLogger) {
			debug = object: DebugConfig {
				override val enabled = enabled
				override val logger = logger
			}
		}

		@Volatile
		private var INSTANCE: ImageLoader? = null

		@Synchronized
		fun get(): ImageLoader {

			return INSTANCE ?: ImageLoader().also {
				INSTANCE = it
				logDebug(TAG, "One Instance initiated and running")
			}

		}

	}

	private val memoryCache = MemoryCache.newInstance()
	private val fileCache = FileCache(context)
	private val bitmapDownloader = BitmapDownloader.newInstance(fileCache)

	private val imageViews = synchronizedMap(WeakHashMap<ImageView, String>())


	fun load(url: String, imageView: ImageView) {
		imageViews[imageView] = url

		memoryCache.get(url)?.let {
			imageView.setImageBitmap(it)
			logDebug(TAG, "Updating from memory cache: ${url.md5()}")
			return
		}

		queuePhoto(url, imageView)
		logDebug(TAG, "Image ${url.md5()} in memory cache is not found. Image in queue.")
	}

	fun preload(url: String) {
		launch {
			bitmapDownloader.preDownload(url)
			logDebug(TAG, "Preloading on ${Thread.currentThread().name}")

		}
	}

	private fun queuePhoto(url: String, imageView: ImageView) {
		val p = ImageToLoad(url, imageView)
		if (imageViewReused(p)) {
			logDebug(TAG, "ImageView reusing")
			return
		}
		launch (MyDispatchers.main()) {
			withContext(MyDispatchers.io()) { getBitmap(url) }?.let {
				updateImageView(it, p)
			}

		}
	}

	private fun getBitmap(url: String): Bitmap? {
		return bitmapDownloader.download(url)?.also { memoryCache.put(url, it) }
	}

	private fun imageViewReused(imageToLoad: ImageToLoad): Boolean {
		val tag = imageViews[imageToLoad.imageView]
		return tag == null || tag != imageToLoad.url
	}

	private fun updateImageView(bitmap: Bitmap?, imageToLoad: ImageToLoad) {
		if (imageViewReused(imageToLoad)) return
		bitmap?.run { imageToLoad.imageView.setImageBitmap(this) }
	}

	fun getFileCacheSize() = fileCache.getFileCacheSizeUsed()

	fun clearCache() {
		memoryCache.clear()
		fileCache.clear()
	}

}