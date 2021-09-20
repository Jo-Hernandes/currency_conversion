package com.example.exchangeratesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.exchangeratesapp.models.ExchangeCurrency
import com.example.exchangeratesapp.ui.main.MainViewModel
import com.example.exchangeratesapp.ui.main.usecases.CalculateRatesUseCase
import com.example.exchangeratesapp.ui.main.usecases.FetchDataUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test
import org.junit.Before


class MainViewModelTest : DefaultSynchronousTest() {

    @RelaxedMockK
    private lateinit var fetchDataUseCase: FetchDataUseCase

    @RelaxedMockK
    private lateinit var calculateRatesUseCase: CalculateRatesUseCase

    private lateinit var handler: MainViewModel

    @Before
    fun `setup handler`() {
        MockKAnnotations.init(this)
        handler = MainViewModel(calculateRatesUseCase, fetchDataUseCase)
    }

    // Single<List<ExchangeCurrency>>
    @Test
    fun `uses datasource to fetch rates`() {
        //when
        handler.fetchRates()

        //then
        verify {
            fetchDataUseCase.getCurrencyItems(any(), any())
        }
    }

    @Test
    fun `updates livedata when exchange list is retrieved`() {
        //given
        val errorObserver = handler.displayException.testObserver

        //when
        handler.fetchRates()

        //then
        verify {
            fetchDataUseCase.fetchRates(any(), any(), any())
            errorObserver wasNot called
        }
    }


    @Test
    fun `throws exception as input field changes with selected currency but no item on list`() {
        //given
        val currentText = "1.0"
        every { calculateRatesUseCase.invoke(any() as ExchangeCurrency, any()) } returns null

        val liveDataObserver = handler.displayCurrencies.testObserver
        val errorObserver = handler.displayException.testObserver

        handler.handleCurrencySelected(
            ExchangeCurrency(
                code = "USD",
                name = "Test Dollar",
                rate = 1.0
            )
        )

        //when
        handler.inputValue = currentText

        //then
        verify {
            (liveDataObserver wasNot called)
            errorObserver.onChanged(any())
        }
    }
}

val <T> LiveData<T>.testObserver get() = mockk<Observer<T>>(relaxed = true).also(this::observeForever)
