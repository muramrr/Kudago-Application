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
import java.lang.ref.SoftReference

/**
 * Bitmap pool to apply to use for bitmaps.
 *
 * @param bitmapsSet a set to hold soft references to bitmaps.
 */
internal class BitmapMemoryPool(private val bitmapsSet: MutableSet<SoftReference<Bitmap>>) {

	companion object {

		/**
		 * Creates a new [BitmapMemoryPool] object.
		 */
		@JvmStatic
		fun newInstance(): BitmapMemoryPool {
			return BitmapMemoryPool(HashSet())
		}
	}

	fun put(bitmap: Bitmap) {
		if (canBePooled(bitmap)) {
			bitmapsSet.add(SoftReference(bitmap))
		}
	}

	/**
	 * Check if the bitmap can be pooled.
	 */
	private fun canBePooled(bitmap: Bitmap): Boolean {
		return bitmap.isMutable && !bitmap.isRecycled
	}

	fun addInBitmapOptions(options: BitmapFactory.Options) {
		// inBitmap only works with mutable bitmaps, so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true
		// Try to find a bitmap to use for inBitmap.
		getReusableBitmap(options)?.let { inBitmap ->
			// If a suitable bitmap has been found, set it as the value of inBitmap.
			options.inBitmap = inBitmap
		}
	}

	/**
	 * @param options - populated [BitmapFactory.Options]
	 * @return Bitmap that case be used for inBitmap
	 */
	private fun getReusableBitmap(options: BitmapFactory.Options): Bitmap? {
		if (bitmapsSet.isEmpty()) return null
		synchronized(bitmapsSet) {
			val iterator: MutableIterator<SoftReference<Bitmap>> = bitmapsSet.iterator()
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
		return byteCount <= allocationByteCount
	}

	/**
	 * Return the byte usage per pixel of a bitmap based on its configuration.
	 */
	private fun bytesPerPixel(config: Bitmap.Config): Int {
		return when (config) {
			Bitmap.Config.ARGB_8888 -> 5
			Bitmap.Config.RGB_565 -> 3
			Bitmap.Config.ALPHA_8 -> 1
			else -> 1
		}
	}

	val size: Int = bitmapsSet.size

	fun clear() {
		bitmapsSet.clear()
	}


}