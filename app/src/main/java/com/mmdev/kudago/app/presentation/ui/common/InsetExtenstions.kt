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

import android.view.View
import android.view.WindowInsets

/**
 * This is the documentation block about the class
 */

fun View.applySystemWindowInsets(
	applyLeft: Boolean = false,
	applyTop: Boolean = false,
	applyRight: Boolean = false,
	applyBottom: Boolean = false
) {
	doOnApplyWindowInsets { targetView, insets, padding ->

		val left = if (applyLeft) insets.systemWindowInsetLeft else 0
		val top = if (applyTop) insets.systemWindowInsetTop else 0
		val right = if (applyRight) insets.systemWindowInsetRight else 0
		val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

		targetView.setPadding(padding.left + left,
		                      padding.top + top,
		                      padding.right + right,
		                      padding.bottom + bottom)
	}
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)


private fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
	// Create a snapshot of the view's padding state
	val initialPadding = recordInitialPaddingForView(this)
	// Set an actual OnApplyWindowInsetsListener which proxies to the given
	// lambda, also passing in the original padding state
	setOnApplyWindowInsetsListener { v, insets ->
		f(v, insets, initialPadding)
		// Always return the insets, so that children can also use them
		insets
	}
	// request some insets
	requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
	InitialPadding(view.paddingLeft, view.paddingTop,
	               view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
	if (isAttachedToWindow) {
		// We're already attached, just request as normal
		requestApplyInsets()
	} else {
		// We're not attached to the hierarchy, add a listener to
		// request when we are
		addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
			override fun onViewAttachedToWindow(v: View) {
				v.removeOnAttachStateChangeListener(this)
				v.requestApplyInsets()
			}

			override fun onViewDetachedFromWindow(v: View) = Unit
		})
	}
}