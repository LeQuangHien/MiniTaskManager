package com.hien.le.minitaskmanager

import android.app.Application
import com.hien.le.minitaskmanager.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MiniTaskManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MiniTaskManagerApplication)
            modules(appModule)
        }
    }
}