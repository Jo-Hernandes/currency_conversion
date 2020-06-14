package com.example.exchangeratesapp.models

data class ExchangeCurrency(
    val code : String,
    val name : String,
    var rate: Double
)
