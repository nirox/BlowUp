package com.mobgen.blowup.di.component

import android.content.Context
import com.mobgen.blowup.di.module.MainModule
import com.mobgen.blowup.presentation.view.gameLauncher.GameLauncherActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    MainModule::class
])
interface MainComponent {

    fun inject(gameLauncher: GameLauncherActivity)

    fun context(): Context


}