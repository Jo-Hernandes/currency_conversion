package com.example.exchangeratesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exchangeratesapp.common.ConnectionException
import com.example.exchangeratesapp.common.asObservable
import com.example.exchangeratesapp.models.ExchangeCurrency
import com.example.exchangeratesapp.ui.main.usecases.CalculateRatesUseCase
import com.example.exchangeratesapp.ui.main.usecases.FetchDataUseCase
import com.hadilq.liveevent.LiveEvent
import java.io.IOException
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

private const val defaultCurrency = "USD"

class MainViewModel(
    private val calculatorUseCase: CalculateRatesUseCase,
    private val fetchDataUseCase: FetchDataUseCase
) : ViewModel() {


    var inputValue: String by "".asObservable {
        _currentCurrency.value?.let { currency ->
            updateList(currency)
        }
    }

    private val _displayCurrencies: MutableLiveData<List<ExchangeCurrency>> = MutableLiveData()
    val displayCurrencies: LiveData<List<ExchangeCurrency>>
        get() = _displayCurrencies

    private val _displayException: LiveEvent<ConnectionException> = LiveEvent()
    val displayException: LiveData<ConnectionException>
        get() = _displayException

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _currentCurrency: MutableLiveData<ExchangeCurrency> = MutableLiveData()
    val currentCurrency: LiveData<ExchangeCurrency>
        get() = _currentCurrency

    init {
        fetchDataUseCase.onFetchStart = { _showLoading.postValue(true) }
        fetchDataUseCase.onFetchEnd = {
            Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .doOnComplete { _showLoading.postValue(false) }.subscribe()
        }

        obtainCurrencyList()
    }

    private fun obtainCurrencyList() {
        fetchDataUseCase.getCurrencyItems(::handleCurrencies) {
            handleError(it, ::obtainCurrencyList)
        }
    }

    private fun handleCurrencies(currencies: List<ExchangeCurrency>) {
        _displayCurrencies.postValue(currencies)
        Completable.complete()
            .delay(1, TimeUnit.SECONDS)
            .doOnComplete { fetchRates() }.subscribe()
    }

    private fun handleError(error: Throwable, retryAction: () -> Unit) {
        _displayException.postValue(
            when (error) {
                is IOException -> ConnectionException.OfflineException(retryAction)
                is java.net.UnknownHostException -> ConnectionException.NoHostException(retryAction)
                else -> ConnectionException.FetchDataException(retryAction)
            }
        )
    }

    private fun handleRatesFetch(rates: List<ExchangeCurrency>) {
        _displayCurrencies.postValue(rates)
        _currentCurrency.postValue(rates.first { it.code == defaultCurrency })
        calculatorUseCase.baseDataList = rates
    }

    private fun updateList(currency: ExchangeCurrency) =
        calculatorUseCase(currency, inputValue.toDoubleOrNull() ?: 0.0)?.let {
            _displayCurrencies.postValue(it)
        } ?: _displayException.postValue(ConnectionException.RatesNotLoaded {
            _currentCurrency.postValue(null)
            fetchRates()
        })

    fun fetchRates() {
        fetchDataUseCase.fetchRates(defaultCurrency, ::handleRatesFetch) {
            handleError(it) { fetchRates() }
        }
    }

    fun handleCurrencySelected(currency: ExchangeCurrency) {
        _currentCurrency.postValue(currency)
        updateList(currency)
    }

    override fun onCleared() {
        fetchDataUseCase.disposeData()
        super.onCleared()
    }

}

