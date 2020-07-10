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

/**
 * Cache that holds references to a number of values.
 */

interface Cache<Key : Any, Value : Any> {

	/**
	 * Returns the value for [key] if it exists in the cache.
	 * If a value was returned, it is moved to the head of the queue.
	 * This returns null if a value is not cached.
	 *
	 * @param key the key to look for.
	 * @return the value corresponding to [key] if exists, otherwise, returns null.
	 */
	fun get(key: Key): Value?

	/**
	 * Caches [value] for [key]. The value is moved to the head of the queue.
	 */
	fun put(key: Key, value: Value)

	/**
	 * Remove the data associated with the key
	 */
	fun evict(key: Key)

	/**
	 * clear cache
	 */
	fun clear()

	/**
	 * Get current cache size
	 *
	 * @return cache current size in bytes.
	 */
	fun size(): Long
}