package com.example.exchangeratesapp.dataSource

import com.example.database.Repository
import com.example.exchangeratesapp.common.observeOnMainThread
import com.example.exchangeratesapp.models.ExchangeCurrency
import io.reactivex.Single

class DataSourceImpl(private val service: Repository) : DataSource {

    private val codeToNameMap: MutableMap<String, String> = mutableMapOf()

    override fun getCurrencyItems(): Single<List<ExchangeCurrency>> = service
        .fetchAvailableCurrencies()
        .observeOnMainThread()
        .map {
            it.map { currency ->
                codeToNameMap[currency.code] = currency.name
                ExchangeCurrency(
                    code = currency.code,
                    name = currency.name,
                    rate = 0.0
                )
            }
        }

    override fun getExchangeRates(code: String): Single<List<ExchangeCurrency>> = service
        .fetchRates(code)
        .observeOnMainThread()
        .map {
            it.map { rate ->
                ExchangeCurrency(
                    code = rate.code,
                    name = codeToNameMap[rate.code] ?: "",
                    rate = rate.rate
                )
            }
        }
}