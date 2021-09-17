package com.example.exchangeratesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.exchangeratesapp.dataSource.DataSource
import com.example.exchangeratesapp.models.ExchangeCurrency
import com.example.exchangeratesapp.ui.main.MainViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Single
import org.junit.Test
import org.junit.Before
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainViewModelTest : DefaultSynchronousTest() {

    @RelaxedMockK
    private lateinit var dataSource: DataSource

    private lateinit var handler: MainViewModel

    @Before
    fun `setup handler`() {
        MockKAnnotations.init(this)
        handler = MainViewModel(dataSource)
    }

    // Single<List<ExchangeCurrency>>
    @Test
    fun `uses datasource to fetch rates`() {
        //given
        every { dataSource.getExchangeRates(any()) } returns Single.just(listOf())

        //when
        handler.fetchRates()

        //then
        verify {
            dataSource.getExchangeRates(any())
        }
    }

    @Test
    fun `updates livedata when exchange list is retrieved`() {
        //given
        val liveDataObserver = handler.displayCurrencies.testObserver
        val errorObserver = handler.displayException.testObserver

        val currentExchangeList = listOf<ExchangeCurrency>()
        every { dataSource.getExchangeRates(any()) } returns Single.just(currentExchangeList)

        //when
        handler.fetchRates()

        //then
        verify {
            liveDataObserver.onChanged(currentExchangeList)
            errorObserver wasNot called
        }
    }

    @Test
    fun `handles error correctly based on webservice response`() {
        //given
        val liveDataObserver = handler.displayCurrencies.testObserver
        val errorObserver = handler.displayException.testObserver

        every { dataSource.getExchangeRates(any()) } returns Single.error(IOException())

        //when
        handler.fetchRates()

        //then
        verify {
            liveDataObserver wasNot called
            errorObserver.onChanged(any())
        }
    }

    @Test
    fun `throws exception as input field changes with selected currency but no item on list`() {
        //given
        val currentText = "1.0"
        val liveDataObserver = handler.displayException.testObserver
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
            liveDataObserver.onChanged(any())
        }
    }
}

val <T> LiveData<T>.testObserver get() = mockk<Observer<T>>(relaxed = true).also(this::observeForever)
