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

package com.mmdev.kudago.app.adapters

import com.mmdev.kudago.app.domain.events.data.EventBaseInfo
import com.mmdev.kudago.app.presentation.ui.events.category_detailed.EventsCategoryDetailedAdapter
import org.junit.Assert.*
import org.junit.Test

/**
 * This is the documentation block about the class
 */

class EventsCategoryDetailedAdapterTest {

	private var adapter = EventsCategoryDetailedAdapter()

	@Test
	fun getItemCount() {

		assertEquals(0, adapter.itemCount)

		adapter = EventsCategoryDetailedAdapter(mutableListOf(EventBaseInfo(), EventBaseInfo()))
		assertEquals(2, adapter.itemCount)
	}

	@Test
	fun getItem() {
		adapter = EventsCategoryDetailedAdapter(mutableListOf(EventBaseInfo(), EventBaseInfo()))
		assertEquals(EventBaseInfo(), adapter.getItem(0))
		assertNotEquals(null, adapter.getItem(1))
	}
}