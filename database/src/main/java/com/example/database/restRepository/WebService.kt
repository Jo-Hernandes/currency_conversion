package com.example.database.restRepository

import com.example.database.dataModel.CurrencyListResponse
import com.example.database.dataModel.ExchangeRateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("list")
    fun getCurrencyList(): Single<CurrencyListResponse>

    @GET("live")
    fun getExchangeRates(
        @Query("source") source: String
    ): Single<ExchangeRateResponse>

}