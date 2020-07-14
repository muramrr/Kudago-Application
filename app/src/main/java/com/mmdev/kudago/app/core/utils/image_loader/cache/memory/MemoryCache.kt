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
import android.util.LruCache
import androidx.core.graphics.BitmapCompat
import com.mmdev.kudago.app.core.utils.image_loader.cache.Cache
import com.mmdev.kudago.app.core.utils.image_loader.cache.bytesToMegabytes
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.logDebug

/**
 * A memory cache which uses a least-recently used eviction policy.
 *
 */
internal class MemoryCache : Cache<String, Bitmap> {

	companion object {
		private const val TAG = "MemoryCache"

		//cache size in bytes
		private val MEMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 8)

		/**
		 * Creates a new [MemoryCache] object.
		 */
		@JvmStatic
		fun newInstance(): MemoryCache = MemoryCache().also {
			logDebug(TAG, "Memory cache initiated with size ${MEMORY_CACHE_SIZE.bytesToMegabytes()} Mb") }

		// Create a cache with a given maximum size in bytes.
		private val bitmapLruCache =
			object : LruCache<String, BitmapAndSize>(MEMORY_CACHE_SIZE.toInt()) {
				override fun sizeOf(key: String, value: BitmapAndSize): Int = value.byteCount
			}
	}


	override fun get(key: String): Bitmap? {
		return bitmapLruCache[key]?.bitmap.also {
			logDebug(TAG, "Trying to get ${key.md5()} from memory cache, result = $it")
		}

	}

	override fun put(key: String, value: Bitmap) {
		val byteCount = BitmapCompat.getAllocationByteCount(value).bytesToMegabytes()
		// If the bitmap is too big for the cache, don't even attempt to store it. Doing so will cause
		// the cache to be cleared. Instead just evict an existing element with the same key if it
		// exists.
		if (byteCount > maxSize()) {
			bitmapLruCache.remove(key)
			logDebug(TAG, "Too big image ${key.md5()} " +
			              "with size $byteCount " +
			              "to store in memory cache ${maxSize()}")
			return
		}
		bitmapLruCache.put(key, BitmapAndSize(value, byteCount)).also {
			logDebug(TAG, "Put to memory cache ${key.md5()}")
		}
	}

	private fun maxSize(): Int = bitmapLruCache.maxSize()

	override fun size(): Int = bitmapLruCache.size()

	override fun evict(key: String) {
		bitmapLruCache.remove(key)
	}

	override fun clear() {
		logDebug(TAG, "Clear memory: ${size()} mb")
		bitmapLruCache.evictAll()
	}

	private data class BitmapAndSize(
		val bitmap: Bitmap,
		val byteCount: Int
	)

}