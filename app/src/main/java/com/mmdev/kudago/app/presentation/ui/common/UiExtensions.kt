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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mmdev.kudago.app.R
import java.util.*

/**
 * This is the documentation block about the class
 */

private val ruLocale = Locale("ru")

fun Context.showToast(toastText: String = "") =
	Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()

@ExperimentalStdlibApi
fun String.capitalizeRu() = this.capitalize(ruLocale)

/**
 * Param:  textView, text to clip from textView, context
 */
fun TextView.attachClickToCopyText(context: Context) {
	this.setOnClickListener {
		val textToClip = this.text.toString()
		val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip = ClipData.newPlainText(textToClip, this.text)
		clipboard.setPrimaryClip(clip)
		Snackbar.make(this, "Copied $textToClip", Snackbar.LENGTH_SHORT).show()
	}
}

fun Context.buildMaterialAlertDialog(title: String, message: String,
                                     positiveText: String?,
                                     positiveClick: () -> Unit?,
                                     negativeText: String?,
                                     negativeClick: () -> Unit?,
                                     neutralText: String?,
                                     neutralClick: () -> Unit?): MaterialAlertDialogBuilder =
	MaterialAlertDialogBuilder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(positiveText) { dialog, _ ->
			positiveClick()
			dialog.dismiss()
		}
		.setNegativeButton(negativeText) { dialog, _ ->
			negativeClick()
			dialog.dismiss()
		}
		.setNeutralButton(neutralText) { dialog, _ ->
			neutralClick()
			dialog.dismiss()
		}

fun Context.showMaterialAlertDialogChooser(items: Array<String>,
                                          clicks: List<()->Unit>): AlertDialog =
	MaterialAlertDialogBuilder(this, R.style.My_MaterialAlertDialog)
		.setItems(items) {
			_, itemIndex ->
			clicks[itemIndex]()
		}
		.create()