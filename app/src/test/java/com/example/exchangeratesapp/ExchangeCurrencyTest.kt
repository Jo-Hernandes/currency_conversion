package com.example.exchangeratesapp

import com.example.exchangeratesapp.models.ExchangeCurrency
import com.example.exchangeratesapp.ui.main.usecases.CalculateRatesUseCase
import io.mockk.*
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.Before


class ExchangeCurrencyTest : DefaultSynchronousTest() {


    private lateinit var calculateRatesUseCase: CalculateRatesUseCase


    @Before
    fun `setup handler`() {
        MockKAnnotations.init(this)
        calculateRatesUseCase = CalculateRatesUseCase()
    }


    @Test
    fun `returns null object when there is no base exchange data list`() {
        //given
        val baseExchange = ExchangeCurrency(
            code = "USD",
            name = "Test Dollar",
            rate = 1.0
        )

        //when
        val list = calculateRatesUseCase(baseExchange, 0.0)

        //then
        assertNull(list)
    }

    @Test
    fun `on base dollar exchange, returns correct value for BRL and JPY`() {
        //given
        val baseExchange = ExchangeCurrency(
            code = "USD",
            name = "Test Dollar",
            rate = 1.0
        )
        calculateRatesUseCase.baseDataList = listOf(
            ExchangeCurrency(
                code = "BRL",
                name = "Test Brazilian Real",
                rate = 5.33
            ),
            ExchangeCurrency(
                code = "JPY",
                name = "Japanese Yen",
                rate = 109.49
            )
        )

        //when
        val ratesList = calculateRatesUseCase(baseExchange, 1.0)

        //then
        assertEquals(listOf(5.33, 109.49), ratesList?.map { it.totalRate.toDouble() })
    }

    @Test
    fun `based on Dollars exchange rates, returns correct values using BLR rates for USD and JPY`(){
        //given

        val baseExchange =  ExchangeCurrency(
            code = "BRL",
            name = "Test Brazilian Real",
            rate = 5.33
        )

        calculateRatesUseCase.baseDataList = listOf(
            ExchangeCurrency(
                code = "USD",
                name = "Test Dollar",
                rate = 1.0
            ),
            ExchangeCurrency(
                code = "JPY",
                name = "Japanese Yen",
                rate = 109.49
            )
        )
        // when
        val ratesList = calculateRatesUseCase(baseExchange, 1.0)

        // then
        assertEquals(listOf(0.19, 20.54), ratesList?.map { it.totalRate.toDouble() })
    }

}
