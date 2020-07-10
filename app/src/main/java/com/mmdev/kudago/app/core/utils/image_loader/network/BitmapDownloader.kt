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
import com.mmdev.kudago.app.core.utils.image_loader.cache.disk.FileCache
import com.mmdev.kudago.app.core.utils.image_loader.cache.md5
import com.mmdev.kudago.app.core.utils.image_loader.logDebug
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Utility class to download an image using its URL.
 */

internal class BitmapDownloader (private val fileCache: FileCache) {

	companion object {
		private const val TAG = "BitmapDownloader"

		private const val USER_REQUEST_TAG = 0xF00D
		private const val CONNECTION_TIMEOUT = 30000 // 30 sec
		private const val READ_TIMEOUT = 30000 // 30 sec


		private const val BUFFER_SIZE = 2048


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
		fun newInstance(fileCache: FileCache): BitmapDownloader = BitmapDownloader(fileCache)
	}

	/**
	 * Check if file exists on disk cache
	 *
	 * else Download an image using its url and decode it to the given width and height.
	 *
	 * @param url image url to download.
	 * @return image as [Bitmap] if success, otherwise null.
	 */
	fun download(url: String): Bitmap? {
		//get from file cache
		val f = fileCache.getFile(url)

		//decode cache file
		val b = fileCache.decodeFile(f)
		if (b != null) {
			logDebug(TAG, "Get from disk cache: ${url.md5()}")
			return b
		}

		//download otherwise
		else{
			logDebug(TAG, "downloading $url as ${url.md5()}")

			var urlConnection: HttpURLConnection? = null
			return try {
				TrafficStats.setThreadStatsTag(USER_REQUEST_TAG)
				val imageUrl = URL(url)
				urlConnection = imageUrl.openConnection() as HttpURLConnection
				urlConnection.apply {
					connectTimeout = CONNECTION_TIMEOUT
					instanceFollowRedirects = true
					readTimeout = READ_TIMEOUT
				}
				val outStream = FileOutputStream(f)
				copyStream(urlConnection.inputStream, outStream)
				outStream.close()
				urlConnection.disconnect()
				return fileCache.decodeFile(f)
			} catch (ex: IOException) {
				logDebug(TAG, "Error while downloading $this \n $ex")
				null
			} finally {
				urlConnection?.disconnect()
				TrafficStats.clearThreadStatsTag()
			}
		}

	}

	fun preDownload(url: String) {
		//get from file cache
		val f = fileCache.getFile(url)
		if (f.exists()) return
		else {
			logDebug(TAG, "Preloading ${url.md5()}")
			download(url)
		}
	}


}