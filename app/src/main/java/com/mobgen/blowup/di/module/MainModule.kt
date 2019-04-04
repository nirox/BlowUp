package com.mobgen.blowup.di.module

import android.content.Context
import com.mobgen.blowup.BlowUpApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(private val application: BlowUpApplication) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return application
    }

}