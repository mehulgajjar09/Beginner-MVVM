package com.mehul.corelibrary.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mehul.corelibrary.R
import com.mehul.corelibrary.databinding.ActivityMainBinding
import com.mehul.corelibrary.core.CoreActivity
import com.mehul.corelibrary.core.CoreViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CoreActivity<ActivityMainBinding>() {
    override fun layoutID(): Int {
        return R.layout.activity_main
    }

    override fun viewModel(): CoreViewModel {
        return ViewModelProvider(this).get(CoreViewModel::class.java)
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        btnOpenFragment.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("first_name","Mehul")
            bundle.putString("last_name","Gajjar")
            val mainBottomSheetFragment = MainBottomSheetFragment.newInstance(bundle)
            mainBottomSheetFragment.show(
                supportFragmentManager,
                mainBottomSheetFragment.tag
            )
        }
    }
}