package com.example.database.cacheRepository

import android.content.Context
import com.example.database.dataModel.*
import io.reactivex.Single
import java.util.*

class PersistenceRepository(private val context: Context) {

    fun getCachedRates(code: String): Single<List<ExchangeRate>> =
        CachedDatabase.getInstance(context).getRatesDao().getRate(code).map {
            it.map { rate ->
                ExchangeRate(rate.exchangeCode, rate.rate)
            }
        }

    fun getCachedExchange(code: String): Single<CachedExchangeCode?> =
        CachedDatabase.getInstance(context).getExchangesDao().getRate().map { it.firstOrNull { exchange -> exchange?.code == code } ?:  CachedExchangeCode(-1, code)}

    fun persistData(exchangeResponse: ExchangeRateResponse) {
        val cachedDatabase = CachedDatabase.getInstance(context)
        cachedDatabase.getExchangesDao().insertData(
            CachedExchangeCode(
                timestamp = Date().time,
                code = exchangeResponse.source
            )
        ).subscribe()

        val ratesDao = cachedDatabase.getRatesDao()
        exchangeResponse.toExchangeList().forEach { rate ->
            ratesDao.insertData(
                CachedExchangeRate(
                    sourceCode = exchangeResponse.source,
                    exchangeCode = rate.code,
                    rate = rate.rate
                )
            ).subscribe()
        }
    }
}