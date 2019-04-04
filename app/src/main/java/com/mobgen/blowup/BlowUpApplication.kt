package com.mobgen.blowup

import android.app.Application
import com.mobgen.blowup.di.component.DaggerMainComponent
import com.mobgen.blowup.di.component.MainComponent
import com.mobgen.blowup.di.module.MainModule

class BlowUpApplication : Application() {
    lateinit var mainComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        mainComponent = DaggerMainComponent.builder().mainModule(MainModule(this)).build()
    }
}