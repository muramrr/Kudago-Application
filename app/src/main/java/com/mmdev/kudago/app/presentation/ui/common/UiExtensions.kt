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

import android.content.ClipData
import android.content.ClipboardManager
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

fun TextView.attachClickToCopyText(context: Context, @StringRes formatter: Int) {
	this.setOnClickListener {
		val copiedText = this.text.toString()
		val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip = ClipData.newPlainText(copiedText, this.text)
		clipboard.setPrimaryClip(clip)
		Snackbar.make(this, getStringRes(formatter).format(copiedText), Snackbar.LENGTH_SHORT).show()
	}
}

fun TextView.setHtmlText(source: String) {
	this.text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT)
	this.movementMethod = LinkMovementMethod.getInstance()
}