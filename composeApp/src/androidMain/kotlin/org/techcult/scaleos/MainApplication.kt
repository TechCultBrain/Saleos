package org.techcult.scaleos

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.techcult.scaleos.core.di.initScaleOsKoin

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initScaleOsKoin()
        {
            androidContext(this@MainApplication)
        }

    }

}