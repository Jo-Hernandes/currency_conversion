package com.example.exchangeratesapp

import android.app.Application
import com.example.exchangeratesapp.depencencyInjection.application
import com.example.exchangeratesapp.depencencyInjection.dataSource
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class CurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@CurrencyApplication)
            modules(dataSource + application)
        }
    }
}


