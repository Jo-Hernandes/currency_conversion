package com.example.database.dataModel


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("quotes")
    val quotes: JsonObject,
    @SerializedName("source")
    val source: String,
    @SerializedName("timestamp")
    val timestamp: Int,
    @SerializedName("success")
    val success: Boolean
)

fun ExchangeRateResponse.toExchangeList() = quotes.keySet().map { key ->
    ExchangeRate(
        code = key.substring(3),
        rate = this.quotes.get(key).asDouble
    )
}
