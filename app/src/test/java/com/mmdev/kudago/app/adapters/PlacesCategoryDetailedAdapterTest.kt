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

import com.mmdev.kudago.app.domain.places.PlaceEntity
import com.mmdev.kudago.app.presentation.ui.places.category_detailed.PlacesCategoryDetailedAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * This is the documentation block about the class
 */

@RunWith(JUnit4::class)
class PlacesCategoryDetailedAdapterTest {


	private var adapter = PlacesCategoryDetailedAdapter()

	@Test
	fun getItemCount() {

		assertEquals(0, adapter.itemCount)

		adapter = PlacesCategoryDetailedAdapter(listOf(PlaceEntity(), PlaceEntity()))
		assertEquals(2, adapter.itemCount)
	}

	@Test
	fun getItem() {
		adapter = PlacesCategoryDetailedAdapter(listOf(PlaceEntity(), PlaceEntity()))
		assertEquals(PlaceEntity(), adapter.getItem(0))
	}

}