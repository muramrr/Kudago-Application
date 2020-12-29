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

package com.mmdev.kudago.app.presentation.ui.common

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun View.getStringRes(@StringRes id: Int) = resources.getString(id)

fun Context.showToast(toastText: String = "") =
	Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()

fun String.capitalizeRu() = this.capitalize(Locale("ru"))

fun TextView.setHtmlText(source: String) {
	this.text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT)
	this.movementMethod = LinkMovementMethod.getInstance()
}

/**
 * Show a SnackBar with [messageRes] resource
 */
fun View.showSnack(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_SHORT) =
	Snackbar.make(this, messageRes, length).show()

/**
 * Show a SnackBar with [message] string
 */
fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_SHORT) =
	Snackbar.make(this, message, length).show()