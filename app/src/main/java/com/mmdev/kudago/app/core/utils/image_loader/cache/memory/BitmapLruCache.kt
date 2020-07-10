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
import com.mmdev.kudago.app.core.utils.image_loader.cache.bytesToMegabytes

/**
 * Bitmap cache that holds strong references to a limited number of values. Each time
 * a value is accessed, it is moved to the head of a queue. When a value is
 * added to a full cache, the value at the end of that queue is evicted and may
 * become eligible for garbage collection.
 *
 * @see https://developer.android.com/topic/performance/graphics/cache-bitmap
 *
 * @param size cache size in MB
 * @param bitmapPool pool of bitmaps to be reused
 */
internal class BitmapLruCache (size: Int, private val bitmapPool: BitmapMemoryPool) :
		LruCache<String, Bitmap>(size) {

	override fun sizeOf(key: String, value: Bitmap): Int {
		return value.allocationByteCount.bytesToMegabytes()
	}

	override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?) {
		oldValue?.let { bitmap ->
			bitmapPool.put(bitmap)
		}
	}
}