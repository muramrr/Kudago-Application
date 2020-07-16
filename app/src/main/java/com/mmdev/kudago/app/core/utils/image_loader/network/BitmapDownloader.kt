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

package com.mmdev.kudago.app.core.utils.image_loader.network

import android.graphics.Bitmap
import android.net.TrafficStats
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.cache.memory.BitmapPool
import com.mmdev.kudago.app.core.utils.image_loader.common.FileDecoder
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Utility class to download an image using its URL.
 */

internal class BitmapDownloader{

	companion object {
		private const val TAG = "BitmapDownloader"

		private const val USER_REQUEST_TAG = 0xF00D
		private const val CONNECTION_TIMEOUT = 30000 // 30 sec
		private const val READ_TIMEOUT = 30000 // 30 sec


		private const val BUFFER_SIZE = 2048


		// Inspired by Guava's ByteStreams.copy()
		// google.github.io/guava/releases/19.0/api/docs/src-html/com/google/common/io/ByteStreams.html#line.103
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

		/**
		 * Creates a new [BitmapDownloader] object.
		 */
		@JvmStatic
		fun newInstance(): BitmapDownloader = BitmapDownloader()
	}


	/**
	 * Download an image using its url and decode it to the given width and height.
	 *
	 * @param url image url to download.
	 * @return image as [Bitmap] if success, otherwise null.
	 */
	fun downloadDecodeCache(url: String, cacheFile: File, bitmapPool: BitmapPool): Bitmap? {
		//new file on disk cache (temp)
		val tempFile = File(cacheFile.path + "TEMP")
		var urlConnection: HttpURLConnection? = null

		var tempOutputStream: OutputStream? = null
		var cacheOutputStream: OutputStream? = null

		return try {
			TrafficStats.setThreadStatsTag(USER_REQUEST_TAG)
			val imageUrl = URL(url)
			urlConnection = imageUrl.openConnection() as HttpURLConnection
			urlConnection.apply {
				connectTimeout = CONNECTION_TIMEOUT
				instanceFollowRedirects = true
				readTimeout = READ_TIMEOUT
			}
			tempOutputStream = FileOutputStream(tempFile)
			copyStream(urlConnection.inputStream, tempOutputStream).also {
				logDebug(TAG, "downloading $url as ${url.md5()}")
			}

			// Decode and scale image to reduce memory consumption
			val bitmap = FileDecoder.decodeAndScaleFile(tempFile, bitmapPool)

			cacheOutputStream = FileOutputStream(cacheFile)

			bitmap?.compress(Bitmap.CompressFormat.PNG, 100, cacheOutputStream)

			return bitmap
		} catch (ex: IOException) {
			logDebug(TAG, "Error while downloading $this \n $ex")
			// Remove cached file if something go wrong
			cacheFile.delete()
//			bitmap?.recycle()
			null
		} finally {
			tempOutputStream?.close()
			cacheOutputStream?.close()
			urlConnection?.disconnect()
			tempFile.delete()
			TrafficStats.clearThreadStatsTag()
		}

	}

	fun preDownload(url: String, cacheFile: File, bitmapPool: BitmapPool): Bitmap? {

		return try {
			downloadDecodeCache(url, cacheFile, bitmapPool).also {
				logDebug(TAG, "Predownloading ${url.md5()}")
			}
		}
		catch (t: Throwable){
			logDebug(TAG, "Error predownloading : ${t.localizedMessage}")
			null
		}

	}


}