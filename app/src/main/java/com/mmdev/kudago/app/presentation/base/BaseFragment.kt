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

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mmdev.kudago.app.presentation.base.mvp.BasePresenter
import com.mmdev.kudago.app.presentation.base.mvp.IBasePresenter
import com.mmdev.kudago.app.presentation.base.mvp.IBaseView

/**
 * generic fragment class
 */

abstract class BaseFragment<Binding : ViewBinding>(
	@LayoutRes private val layoutId: Int
) : Fragment(layoutId), IBaseView {

	protected val TAG = "mylogs_" + javaClass.simpleName

	protected open val presenter: BasePresenter<*>? = null
	
	protected var _binding: Binding? = null
	
	protected val viewBinding: Binding
		get() = _binding ?: throw IllegalStateException(
			"Trying to access the binding outside of the view lifecycle."
		)
	
	
	protected lateinit var navController: NavController

	@Suppress("UNCHECKED_CAST")
	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		navController = findNavController()
		(presenter as IBasePresenter<IBaseView>?)?.linkView(this)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViews()
	}
	
	abstract fun setupViews()

	override fun onDestroyView() {
		_binding = null
		
		presenter?.unlinkView()
		super.onDestroyView()
	}

	

}

