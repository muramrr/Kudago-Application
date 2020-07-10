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
import android.util.Log
import com.mmdev.kudago.app.core.utils.image_loader.cache.Cache

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param bitmapPool bitmap pool to hold references to bitmaps.
 * @param bitmapLruCache bitmap LRU cache.
 */
internal class MemoryCache (private val bitmapPool: BitmapMemoryPool,
                            private val bitmapLruCache: BitmapLruCache) : Cache<String, Bitmap> {

	companion object {
		private const val TAG = "MemoryCache"

		private val MEMORY_CACHE_SIZE = ((Runtime.getRuntime().maxMemory() / 8) / 1024).toInt()

		/**
		 * Creates a new [MemoryCache] object.
		 */
		@JvmStatic
		fun newInstance(bitmapPool: BitmapMemoryPool = BitmapMemoryPool.newInstance(),
		                size: Int = MEMORY_CACHE_SIZE): MemoryCache {

			val bitmapLruCache = BitmapLruCache(size, bitmapPool)
			return MemoryCache(bitmapPool, bitmapLruCache)
		}
	}

	fun getBitmapPool() = bitmapPool

	override fun get(key: String): Bitmap? {
		return bitmapLruCache.get(key)
	}

	override fun put(key: String, value: Bitmap) {
		bitmapLruCache.put(key, value)
	}

	override fun size(): Long {
		return bitmapLruCache.size().toLong() / 1024
	}

	override fun evict(key: String) {
		bitmapLruCache.remove(key)
	}

	override fun clear() {
		Log.i(TAG, "Clear memory: ${size()} mb")
		bitmapLruCache.evictAll()
		Log.i(TAG, "Clear bitmap pool: ${bitmapPool.size} elements")
		bitmapPool.clear()
	}

}