package com.example.database.dataModel

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyListResponse(
    @SerializedName("currencies")
    val currencies: JsonObject,
    @SerializedName("success")
    val success: Boolean
)

fun CurrencyListResponse.toCurrencyList() = currencies.keySet().map { key ->
    CurrencyItem(
        code = key,
        name = this.currencies.get(key).asString
    )
}
