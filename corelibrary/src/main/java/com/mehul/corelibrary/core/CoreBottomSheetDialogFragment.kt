package com.mehul.corelibrary.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mehul.corelibrary.R

abstract class CoreBottomSheetDialogFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {

    private lateinit var binding: T

    abstract fun layoutID(): Int

    abstract fun viewModel(): CoreViewModel

    abstract fun initFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate<T>(inflater, layoutID(), container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.handler, this)
        return binding.root
    }

    fun getViewModel() = viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.color.black_transparent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel())
        initFragment()
    }

    fun startActivity(cls: Class<*>) {
        getAct().startActivity(cls)
    }

    private fun getAct(): CoreActivity<*> {
        return activity as CoreActivity<*>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.CustomBottomSheetDialog)
    }

    fun showProgressBar() {
        (activity as CoreActivity<*>?)?.showProgressBar()
    }

    fun hideProgressBar() {
        (activity as CoreActivity<*>?)?.hideProgressBar()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(this.javaClass.simpleName) == null)
            super.show(manager, this.javaClass.simpleName)
    }
}