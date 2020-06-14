package com.example.exchangeratesapp.ui.main

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exchangeratesapp.common.ConnectionException
import com.example.exchangeratesapp.common.asObservable
import com.example.exchangeratesapp.common.observeOnMainThread
import com.example.exchangeratesapp.dataSource.DataSource
import com.example.exchangeratesapp.models.ExchangeCurrency
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.io.IOException

class MainViewModel(private val dataSource: DataSource) : ViewModel() {

    init {
        obtainCurrencyList()
    }

    private val _updateValue: MutableLiveData<String> = MutableLiveData()
    val updateValue: LiveData<String>
        get() = _updateValue

    var inputValue: String by "".asObservable(_updateValue::postValue)

    private val _displayCurrencies: MutableLiveData<List<ExchangeCurrency>> = MutableLiveData()
    val displayCurrencies: LiveData<List<ExchangeCurrency>>
        get() = _displayCurrencies

    private val _displayException: LiveEvent<ConnectionException> = LiveEvent()
    val displayException: LiveData<ConnectionException>
        get() = _displayException

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private var listDisposable: Disposable? = null
        set(value) {
            listDisposable?.dispose()
            field = value
        }

    private var ratesDisposable: Disposable? = null
        set(value) {
            ratesDisposable?.dispose()
            field = value
        }

    private fun obtainCurrencyList() {
        listDisposable = dataSource
            .getCurrencyItems()
            .subscribe(::handleCurrencies) {
                handleError(it, ::obtainCurrencyList)
            }
    }

    private fun handleCurrencies(currencies: List<ExchangeCurrency>) =
        _displayCurrencies.postValue(currencies)

    private fun handleError(error: Throwable, retryAction: () -> Unit) {
        _displayException.postValue(
            when (error) {
                is IOException -> ConnectionException.OfflineException(retryAction)
                is java.net.UnknownHostException -> ConnectionException.NoHostException(retryAction)
                else -> ConnectionException.FetchDataException(retryAction)
            }
        )
    }

    fun fetchRates(code: String) {
        ratesDisposable =
            dataSource.getExchangeRates(code).withLoading().observeOnMainThread()
                .subscribe(::handleCurrencies) {
                    handleError(it) { fetchRates(code) }
                }
    }

    override fun onCleared() {
        super.onCleared()
        listDisposable?.dispose()
        ratesDisposable?.dispose()
    }

    private fun <T> Single<T>.withLoading() = this.doOnSubscribe {
        _showLoading.postValue(true)
    }.doFinally {
        Handler().postDelayed({ //adds a small delay, so the it doesnt hiccups when the call is made too fast
            _showLoading.postValue(false)
        }, 200)
    }

}

