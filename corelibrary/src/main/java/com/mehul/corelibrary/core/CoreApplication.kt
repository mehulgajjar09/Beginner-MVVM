package com.mehul.corelibrary.core

import android.app.Application

open class CoreApplication : Application() {

    companion object Singleton {
        private lateinit var app: CoreApplication
        fun getInstance(): CoreApplication {
            return app
        }
    }
}