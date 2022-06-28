package com.mehul.corelibrary.core

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


abstract class CoreDialogFragment<T : ViewDataBinding> : DialogFragment() {

    abstract fun layoutID(): Int

    abstract fun viewModel(): CoreViewModel

    abstract fun initFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<T>(inflater, layoutID(), container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel())
        binding.setVariable(BR.handler, this)
        return binding.root
    }

    fun getViewModel() = viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    fun startActivity(cls: Class<*>) {
        getAct().startActivity(cls)
    }

    private fun getAct(): CoreActivity<*> {
        return activity as CoreActivity<*>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // creating the fullscreen dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return dialog
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