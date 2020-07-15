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
import com.mmdev.kudago.app.core.utils.image_loader.cache.bytesToMegabytes
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.lang.ref.SoftReference
import java.util.Collections.synchronizedSet

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param bitmapPool bitmap pool to hold references to bitmaps.
 * @param bitmapLruCache bitmap LRU cache.
 */
internal class MemoryCache : Cache<String, Bitmap> , BitmapPool {

	companion object {
		private const val TAG = "MemoryCache"

		private val MEMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 8)


		/**
		 * Creates a new [MemoryCache] object.
		 */
		@JvmStatic
		fun newInstance(): MemoryCache {

			return MemoryCache()
				.also {
					logDebug(TAG, "Memory cache initiated with size ${MEMORY_CACHE_SIZE.bytesToMegabytes()} Mb")
				}
		}

		private val bitmapLruCache = object : LruCache<String, Bitmap>(MEMORY_CACHE_SIZE.toInt()) {

			override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap,
			                          newValue: Bitmap) {

				if (oldValue.isMutable && !oldValue.isRecycled)
					bitmapsPool.add(SoftReference(oldValue))

			}

			//override fun sizeOf(key: String, value: Bitmap): Int = value.allocationByteCount
		}

		//a set to hold soft references to bitmaps.
		private val bitmapsPool: MutableSet<SoftReference<Bitmap>> = synchronizedSet(HashSet())
	}


	override fun get(key: String): Bitmap? {
		return bitmapLruCache.get(key).also {
			logDebug(TAG, "Trying to get ${key.md5()} from memory cache, result = $it")
		}

	}

	override fun put(key: String, value: Bitmap) {
		bitmapLruCache.put(key, value).also {
			logDebug(TAG, "Put to memory cache ${key.md5()}")
		}
	}

	override fun size(): Int = bitmapLruCache.size()

	private fun bitmapsPoolSize() = bitmapsPool.size

	override fun evict(key: String) {
		bitmapLruCache.remove(key)
	}

	override fun clear() {
		logDebug(TAG, "Clear memory: ${size()} mb")
		bitmapLruCache.evictAll()
		logDebug(TAG, "Clear bitmap pool: ${bitmapsPoolSize()} elements")
		bitmapsPool.clear()
	}


	/**
	 * @param options - populated [BitmapFactory.Options]
	 * @return Bitmap that case be used for inBitmap
	 */
	// This method iterates through the reusable bitmaps, looking for one to use for inBitmap:
	override fun getReusableBitmap(options: BitmapFactory.Options): Bitmap? {
		var bitmap: Bitmap? = null
		if (bitmapsPool.isNotEmpty()) {
			synchronized(bitmapsPool) {
				val iterator: MutableIterator<SoftReference<Bitmap>> = bitmapsPool.iterator()
				var item: Bitmap?
				while (iterator.hasNext()) {
					item = iterator.next().get()
					if (item != null && item.isMutable) {
						// Check to see it the item can be used for inBitmap.
						if (item.canUseForInBitmap(options)) {
							bitmap = item

							// Remove from reusable set so it can't be used again.
							iterator.remove()
							break
						}
					}
					// Remove from the set if the reference has been cleared.
					else iterator.remove()
				}
			}
		}
		logDebug(TAG, "bitmap reused = ${bitmap != null}")
		logDebug(TAG, "bitmap pool size = ${bitmapsPool.size}")
		return bitmap
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
		return byteCount <= allocationByteCount
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