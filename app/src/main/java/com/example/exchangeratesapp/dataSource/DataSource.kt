package com.example.exchangeratesapp.dataSource

import com.example.exchangeratesapp.models.ExchangeCurrency
import io.reactivex.Single

interface DataSource {
    fun getCurrencyItems(): Single<List<ExchangeCurrency>>

    fun getExchangeRates(code: String): Single<List<ExchangeCurrency>>
}
