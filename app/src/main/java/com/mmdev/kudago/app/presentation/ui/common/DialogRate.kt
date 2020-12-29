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

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mmdev.kudago.app.R
import com.mmdev.kudago.app.core.KudagoApp

/**
 * Dialog used for rating application
 */

class DialogRate: DialogFragment() {
	
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return MaterialAlertDialogBuilder(requireContext(), R.style.My_MaterialAlertDialog)
			.setView(R.layout.dialog_rate)
			.setTitle(R.string.dialog_rate_title)
			.setMessage(R.string.dialog_rate_message)
			.setPositiveButton(R.string.dialog_rate_btn_positive) { dialog, button ->
				
				if (requireDialog().findViewById<RatingBar>(R.id.ratingBar).rating > 3) {
					var link = "market://details?id="
					try {
						// play market available
						requireContext().packageManager.getPackageInfo("com.android.vending", 0)
						// not available
					} catch (e: PackageManager.NameNotFoundException) {
						e.printStackTrace()
						// should use browser
						link = "https://play.google.com/store/apps/details?id="
					}
					//mark dialog was already interacted
					KudagoApp.isRateDialogAvailable = false
					
					// starts external action
					requireContext().startActivity(
						Intent(
							Intent.ACTION_VIEW,
							Uri.parse(link + requireContext().packageName)
						)
					)
				}
				else requireParentFragment()
					.requireView()
					.showSnack(R.string.dialog_rate_snack_text)
				
			}
			.setNegativeButton(R.string.dialog_rate_btn_negative, null)
			.setNeutralButton(R.string.dialog_rate_btn_neutral, null)
			.create()
	}
	
}