package com.mehul.corelibrary.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mehul.corelibrary.R
import com.mehul.corelibrary.core.CoreBottomSheetDialogFragment
import com.mehul.corelibrary.core.CoreViewModel
import com.mehul.corelibrary.databinding.BottomSheetFragmentMainBinding
import kotlinx.android.synthetic.main.bottom_sheet_fragment_main.*

class MainBottomSheetFragment : CoreBottomSheetDialogFragment<BottomSheetFragmentMainBinding>() {
    override fun layoutID(): Int {
        return R.layout.bottom_sheet_fragment_main
    }

    override fun viewModel(): CoreViewModel {
        return ViewModelProvider(this).get(CoreViewModel::class.java)
    }

    override fun initFragment() {
        arguments?.let {
            tvFirstNameLastName.text = it.get("first_name").toString() + " " + it.get("last_name").toString()
        }
    }

    companion object {
        var mainBottomSheetFragment: MainBottomSheetFragment? = null

        @JvmStatic
        fun newInstance(bundle: Bundle): MainBottomSheetFragment {
            if (mainBottomSheetFragment == null) {
                mainBottomSheetFragment = MainBottomSheetFragment()
            }
            mainBottomSheetFragment?.arguments = bundle
            return mainBottomSheetFragment as MainBottomSheetFragment
        }
    }
}