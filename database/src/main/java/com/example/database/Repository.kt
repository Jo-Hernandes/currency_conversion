package com.example.database

import com.example.database.dataModel.CurrencyItem
import com.example.database.dataModel.ExchangeRate
import io.reactivex.Single

interface Repository {

    fun fetchAvailableCurrencies() : Single<List<CurrencyItem>>

    fun fetchRates(currencyCode: String) : Single<List<ExchangeRate>>

}
