/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 10.04.20 17:27
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.kudago.app.domain.places

/**
 * Places entities
 * Places response - list of PlaceItem received from api call
 */

data class PlaceEntity (val id: Int = 0,
                        val title: String = "",
                        val short_title: String = "",
                        val images: List<ImageEntity> = emptyList())

data class PlacesResponse (val results: List<PlaceEntity> = emptyList())

data class PlaceDetailedEntity (val id: Int = 0,
                                val title: String = "",
                                val short_title: String = "",
                                val body_text: String = "",
                                val description: String = "",
                                val images: List<ImageEntity> = emptyList())

//image url
//do not change because it is api declaration
data class ImageEntity (val image: String = "")