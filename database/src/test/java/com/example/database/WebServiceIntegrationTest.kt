package com.example.database

import com.example.database.restRepository.WebService
import com.example.database.restRepository.client.RestServiceImpl
import com.example.database.restRepository.client.WebClient
import org.junit.Test
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WebServiceIntegrationTest : DefaultSynchronousTest() {

    private lateinit var service: WebService

    @Before
    fun `init webService object`() {
        service = RestServiceImpl(WebClient()).provideWebService()
    }

    @Test
    fun `returns success if there is internet connection available`() {
        service.getCurrencyList()
            .test()
            .assertNoTimeout()
            .assertValue { it.success }
    }

    @Test
    fun `returns success if the code entered is valid`() {
        service.getExchangeRates("USD")
            .test()
            .assertNoTimeout()
            .assertValue { it.success }
    }

    @Test
    fun `returns error if code entered is invalid`() {
        service.getExchangeRates("JJBA")
            .test()
            .assertNoTimeout()
            .assertValue { !it.success }
    }
}
