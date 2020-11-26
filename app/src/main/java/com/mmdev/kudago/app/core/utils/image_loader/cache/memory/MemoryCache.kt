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

package com.mmdev.kudago.app.core.utils.image_loader.cache.memory

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import com.mmdev.kudago.app.core.utils.image_loader.cache.Cache
import com.mmdev.kudago.app.core.utils.log.logDebug
import java.lang.ref.SoftReference
import java.util.Collections.synchronizedSet

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param bitmapPool bitmap pool to hold references to bitmaps.
 * @param bitmapLruCache bitmap LRU cache.
 */
class MemoryCache : Cache<String, Bitmap> , BitmapPool {

	private val TAG = "mylogs_${javaClass.simpleName}"
	
	companion object {

		private val MEMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 8) / 1024

		/**
		 * Creates a new [MemoryCache] object.
		 */
		@JvmStatic
		fun newInstance(): MemoryCache {

			return MemoryCache().also {
				logDebug(it.javaClass, "Memory cache initiated with size ${MEMORY_CACHE_SIZE/1024} Mb")
			}
		}

	}

	//a set to hold soft references to bitmaps.
	private var reusableBitmaps = synchronizedSet(HashSet<SoftReference<Bitmap>>())

	private val bitmapLruCache = object : LruCache<String, Bitmap>(MEMORY_CACHE_SIZE.toInt()) {

		override fun entryRemoved(
			evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?
		) {
			oldValue?.let {
				if (it.isMutable && !it.isRecycled) reusableBitmaps.add(SoftReference(oldValue))
			}

		}
		// The cache size will be measured in kilobytes rather than number of items.
		override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount/1024
	}


	override fun get(key: String): Bitmap? {
		return bitmapLruCache.get(key).also {
			//logDebug(TAG, "Trying to get ${key.md5()} from memory cache, result = $it")
		}

	}

	override fun put(key: String, value: Bitmap) {
		bitmapLruCache.put(key, value).also {
			//logDebug(TAG, "Put to memory cache ${key.md5()}")
		}
	}

	override fun size(): Int = bitmapLruCache.size()/1024 //in megabytes

	private fun bitmapsPoolSize() = reusableBitmaps.size

	override fun evict(key: String) {
		bitmapLruCache.remove(key)
	}

	override fun clear() {
		logDebug(TAG, "Clear memory: ${size()} mb")
		bitmapLruCache.evictAll()
		logDebug(TAG, "Clear bitmap pool: ${bitmapsPoolSize()} elements")
		reusableBitmaps.clear()
	}


	/**
	 * @param options - populated [BitmapFactory.Options]
	 * @return Bitmap that case be used for inBitmap
	 */
	// This method iterates through the reusable bitmaps, looking for one
	// to use for inBitmap:
	override fun getReusableBitmap(options: BitmapFactory.Options): Bitmap? {
		reusableBitmaps.takeIf { it.isNotEmpty() }?.let { reusableBitmaps ->
			synchronized(reusableBitmaps) {
				//logDebug(TAG, "Searching for reusable bitmap...")
				val iterator: MutableIterator<SoftReference<Bitmap>> = reusableBitmaps.iterator()
				while (iterator.hasNext()) {
					iterator.next().get()?.let { item ->
						if (item.isMutable) {
							// Check to see it the item can be used for inBitmap.
							if (item.canUseForInBitmap(options)) {
								// Remove from reusable set so it can't be used again.
								iterator.remove()
								return item
							}
						} else {
							// Remove from the set if the reference has been cleared.
							iterator.remove()
						}
					}
				}
			}
		}
		return null
	}

	/**
	 * From Android 4.4 (KitKat) onward we can re-use if the byte size of the new bitmap
	 * is smaller than the reusable bitmap candidate allocation byte count.
	 * @param targetOptions options that have the out* value populated
	 * @return true if [Bitmap] can be used for inBitmap re-use with [targetOptions]
	 */
	private fun Bitmap.canUseForInBitmap(targetOptions: BitmapFactory.Options): Boolean {
		if (targetOptions.inSampleSize < 1) targetOptions.inSampleSize = 1
		val width = targetOptions.outWidth / targetOptions.inSampleSize
		val height = targetOptions.outHeight / targetOptions.inSampleSize
		val byteCount: Int = width * height * bytesPerPixel(config)
		return (byteCount <= allocationByteCount)
	}

	/**
	 * Return the byte usage per pixel of a bitmap based on its configuration.
	 */
	private fun bytesPerPixel(config: Bitmap.Config): Int {
		return when (config) {
			Bitmap.Config.ARGB_8888 -> 4
			Bitmap.Config.RGB_565 -> 2
			Bitmap.Config.ALPHA_8 -> 1
			else -> 1
		}
	}
}