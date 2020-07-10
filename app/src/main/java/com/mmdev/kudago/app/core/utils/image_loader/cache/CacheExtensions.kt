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

package com.mmdev.kudago.app.core.utils.image_loader.cache

import java.math.BigInteger
import java.security.MessageDigest



internal fun Int.bytesToMegabytes(): Int { return this / 1024 / 1024 }

internal fun Long.bytesToMegabytes(): Int { return this.toInt() / 1024 / 1024 }

/**
 * Hash a string to an MD5.
 *
 * simple hashCode() not guarantee unique code per object which can be reused later
 *
 * @return MD5 hash corresponding to the given string
 */
internal fun String.md5(): String {
	val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
	val digest: ByteArray = messageDigest.digest(toByteArray())
	return BigInteger(1, digest).toString(16).padStart(32, '0')
}