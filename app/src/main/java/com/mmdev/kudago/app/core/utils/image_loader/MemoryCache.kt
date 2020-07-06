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

import android.graphics.Bitmap
import android.util.Log
import java.util.Collections.synchronizedMap

/**
 * Custom memory cache depends on device ram
 */

class MemoryCache {
	private val cache =
		synchronizedMap(LinkedHashMap<String, Bitmap>(10, 1.5f, true))
	private var size: Long = 0
	private var limit: Long = 10 * 1024 * 1024

	//set limit depending on memory limit of device
	init { setLimit(Runtime.getRuntime().maxMemory() / 4) }

	companion object {
		private const val TAG = "MemoryCache"
	}

	private fun setLimit(new_limit: Long) {
		if (limit<new_limit) limit = new_limit
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024.0 / 1024.0 + "MB")
	}

	operator fun get(id: String): Bitmap? {
		return try {
			if (!cache.containsKey(id)) null
			else cache[id]
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
			null
		}

	}

	fun put(id: String, bitmap: Bitmap) {
		try {
			if (cache.containsKey(id)) size -= getSizeInBytes(cache[id])
			cache[id] = bitmap
			size += getSizeInBytes(bitmap)
			checkSize()
		} catch (th: Throwable) {
			th.printStackTrace()
		}

	}

	private fun checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size)
		if (size > limit) {
			val iter = cache.entries.iterator()
			while (iter.hasNext()) {
				size -= getSizeInBytes(iter.next().value)
				iter.remove()

				if (size <= limit) break
			}
			Log.i(TAG, "Clean cache. New size " + cache.size)
		}
	}

	fun clear() {
		try {
			cache.clear()
			size = 0
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
		}

	}

	private fun getSizeInBytes(bitmap: Bitmap?): Long {
		return if (bitmap == null) 0 else (bitmap.rowBytes * bitmap.height).toLong()
	}


}