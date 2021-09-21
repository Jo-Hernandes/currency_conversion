package com.example.exchangeratesapp.ui.main.usecases

import com.example.exchangeratesapp.dataSource.DataSource
import com.example.exchangeratesapp.models.ExchangeCurrency
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class FetchDataUseCase(private val dataSource: DataSource) {

    var onFetchStart: (() -> Unit)? = null
    var onFetchEnd: (() -> Unit)? = null

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

    fun disposeData() {
        listDisposable?.dispose()
        ratesDisposable?.dispose()
    }

    fun getCurrencyItems(
        onSuccess: (List<ExchangeCurrency>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        listDisposable =
            dataSource
                .getCurrencyItems()
                .doOnSubscribe(onFetchStart)
                .doOnFinally(onFetchEnd)
                .subscribe(onSuccess, onError)
    }

    fun fetchRates(
        currency: String,
        onSuccess: (List<ExchangeCurrency>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        ratesDisposable =
            dataSource.getExchangeRates(currency)
                .doOnSubscribe(onFetchStart)
                .doOnFinally(onFetchEnd)
                .subscribe(onSuccess, onError)

    }

    private fun <T> Single<T>.doOnSubscribe(
        onStart: (() -> Unit)?
    ) = this.doOnSubscribe {
        onStart?.invoke()
    }

    private fun <T> Single<T>.doOnFinally(
        onFinally: (() -> Unit)?
    ) = this.doOnSubscribe {
        onFinally?.invoke()
    }

}
