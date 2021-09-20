package com.example.exchangeratesapp.ui.main.usecases

import com.example.exchangeratesapp.models.ExchangeCurrency

class CalculateRatesUseCase {

    var baseDataList: List<ExchangeCurrency>? = null

    operator fun invoke(
        baseExchange: ExchangeCurrency,
        currentValue: Double
    ): List<ExchangeCurrency>? = baseDataList?.map {
        it.copy(totalRate = String.format("%.2f", (it.rate / baseExchange.rate) * currentValue))
    }
}
