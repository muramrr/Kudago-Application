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

package com.mmdev.kudago.app.presentation.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * pass the MyBinding.bind function reference to our delegate,
 * and initialize the View’s binding using [viewBinding] function.
 * Then, we clear this binding value when the views are destroyed.
 * Kotlin Delegated Property in use
 */

class FragmentViewBindingDelegate<T : ViewBinding>(val fragment: Fragment,
                                                   val viewBindingFactory: (View) -> T) :
		ReadOnlyProperty<Fragment, T> {

	private var binding: T? = null

	init {
		//add observer to fragment
		fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
			override fun onCreate(owner: LifecycleOwner) {
				fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
					viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
						//null value
						//remove observer
						override fun onDestroy(owner: LifecycleOwner) {
							owner.lifecycle.removeObserver(this)
							binding = null
						}
					})
				}
			}
		})
	}

	override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
		val binding = binding
		if (binding != null) {
			return binding
		}

		val lifecycle = fragment.viewLifecycleOwner.lifecycle
		if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
			throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
		}

		return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
	}
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
	FragmentViewBindingDelegate(this, viewBindingFactory)