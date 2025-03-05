package com.setgreet.example

import android.app.Application
import com.setgreet.Setgreet
import com.setgreet.model.SetgreetConfig

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Setgreet.initialize(
            context = this,
            appKey = "APP_KEY",
            config = SetgreetConfig(
                debugMode = true
            )
        )
    }
}
