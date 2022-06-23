package com.mehul.beginnermvvm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mehul.beginnermvvm.databinding.ActivityMainBinding
import com.mehul.corelibrary.core.CoreActivity
import com.mehul.corelibrary.core.CoreViewModel

class MainActivity : CoreActivity<ActivityMainBinding>() {
    override fun layoutID(): Int {
        return R.layout.activity_main
    }

    override fun viewModel(): CoreViewModel {
        return ViewModelProvider(this).get(CoreViewModel::class.java)
    }

    override fun initActivity(savedInstanceState: Bundle?) {

    }
}