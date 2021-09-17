package com.example.exchangeratesapp.models

data class ExchangeCurrency(
    val code: String = "",
    val name: String = "",
    var rate: Double = 0.0,
    var totalRate: String = ""
)
