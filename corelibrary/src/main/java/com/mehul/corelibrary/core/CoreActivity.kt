package com.mehul.corelibrary.core

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mehul.corelibrary.R
import java.io.Serializable
import androidx.databinding.library.baseAdapters.BR

/*
*
*   This activity is the base of all activity, While extending it to another activity
*   please pass corresponding activity's auto generated Data Binding Class.
*   also please do not forgot to pass ViewModel as we have use ViewModel in XML.
*
*/

abstract class CoreActivity<T : ViewDataBinding> : AppCompatActivity() {

    private var layout: RelativeLayout? = null
    private var dialog: Dialog? = null

    abstract fun layoutID(): Int

    abstract fun viewModel(): CoreViewModel

    abstract fun initActivity(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<T>(this, layoutID())
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel())
        binding.setVariable(BR.handler, this)

        initActivity(savedInstanceState)
    }

    fun getViewModel(): Any = viewModel()

    fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
        overridePendingTransitionEnter()
    }

    fun startActivityWithData(cls: Class<*>, obj: Any) {
        val intent = Intent(this, cls)
        if (obj is Serializable) intent.putExtra("Extras", obj)
        startActivity(intent)
        overridePendingTransitionEnter()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */


    private fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    private fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    fun overridePendingTransitionDown() {
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showProgressBar() {
        if (dialog?.isShowing == true) {
            return
        }
        dialog = Dialog(this)
        val inflate = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null)
        dialog?.setContentView(inflate)
        dialog?.setCancelable(false)
        dialog?.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.show()
    }

    fun hideProgressBar() {
        dialog?.dismiss()
        dialog = null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}