package com.example.exchangeratesapp.depencencyInjection

import com.example.exchangeratesapp.ui.main.MainViewModel
import org.koin.dsl.module

val application = module{

    factory { MainViewModel(get()) }

}