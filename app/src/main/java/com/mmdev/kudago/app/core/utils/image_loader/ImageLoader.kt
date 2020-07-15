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
import com.mmdev.kudago.app.core.utils.image_loader.cache.memory.MemoryCache
import com.mmdev.kudago.app.core.utils.image_loader.common.DebugConfig
import com.mmdev.kudago.app.core.utils.image_loader.common.MyLogger
import com.mmdev.kudago.app.core.utils.image_loader.network.BitmapDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import java.util.Collections.synchronizedMap
import kotlin.coroutines.CoroutineContext


class ImageLoader : KoinComponent, CoroutineScope {

	override val coroutineContext: CoroutineContext
		get() = MyDispatchers.io()


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
		memoryCache.get(url)?.let { imageView.setImageBitmap(it) } ?: queuePhoto(url, imageView)
	}

	fun preload(url: String) {
		launch {
			bitmapDownloader.preDownload(url, memoryCache)//?.also { memoryCache.put(url, it) }
		}
	}

	private fun queuePhoto(url: String, imageView: ImageView) {
		val imageData = ImageData(url, imageView)
		//needed to clear recycler views that has already loaded image previously
		imageView.setImageResource(0)
		if (imageViewReused(imageData)) {
			logDebug(TAG, "ImageView reused")
			return
		}
		launch (MyDispatchers.main()) {
			// Check if image already downloaded
			if (imageViewReused(imageData)) return@launch
			// Download image from web url
			withContext(MyDispatchers.io()) { getBitmap(url) }?.let {

				if (imageViewReused(imageData)) return@launch
				updateImageView(it, imageData)
				memoryCache.put(url, it)
			}

		}
	}

	private fun getBitmap(url: String): Bitmap? {
		return try {
			fileCache.getBitmapFromDiskCache(url, memoryCache) ?:
			bitmapDownloader.downloadAndDecode(url, memoryCache)
		} catch (e: OutOfMemoryError) {
			logDebug(TAG, "Out of memory error, init gc & memory cache clear")
			memoryCache.clear()
			System.gc()
			logDebug(TAG, "Trying to load again...")
			getBitmap(url)
		}
	}

	// Used for avoiding collisions with reusable imageViews or multiple loads
	private fun imageViewReused(imageData: ImageData): Boolean {
		val associatedUrl = imageViews[imageData.imageView]
		// Check is url exist in relationsMap and equals to url from holder
		return associatedUrl == null || associatedUrl != imageData.url
	}

	private fun updateImageView(bitmap: Bitmap?, imageData: ImageData) {
		if (!imageViewReused(imageData)){
			// Remove relation for avoiding multiple loads
			imageViews.remove(imageData.getWeakImageView())

			// Show bitmap on UI
			bitmap?.let {
				imageData.getWeakImageView()?.setImageBitmap(it)
			}
		}

	}

	fun getFileCacheSize() = fileCache.getFileCacheSizeUsed()

	fun clearCache() {
		memoryCache.clear()
		fileCache.clear()
	}

}