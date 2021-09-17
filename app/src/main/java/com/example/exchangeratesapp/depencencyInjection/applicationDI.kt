package com.example.exchangeratesapp.depencencyInjection

import com.example.exchangeratesapp.ui.main.MainViewModel
import com.example.exchangeratesapp.ui.main.usecases.CalculateRatesUseCase
import com.example.exchangeratesapp.ui.main.usecases.FetchDataUseCase
import org.koin.dsl.module

val application = module {

    factory { MainViewModel(get(), get()) }

    factory { CalculateRatesUseCase() }
    factory { FetchDataUseCase(get()) }
}
