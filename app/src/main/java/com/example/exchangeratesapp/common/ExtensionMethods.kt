package com.example.exchangeratesapp.common

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.exchangeratesapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

fun <T> Single<T>.observeOnMainThread(): Single<T> = subscribeOn(Schedulers.io()).observeOn(
    AndroidSchedulers.mainThread()
)

fun <T> Fragment.observe(liveData: LiveData<T>, action: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { action(it) })
}

fun Fragment.showSnackbar(
    @StringRes text: Int,
    duration: Int = BaseTransientBottomBar.LENGTH_LONG,
    onRetry: (() -> Unit)? = null
) = view?.let { Snackbar.make(it, text, duration) }?.also { snackbar ->
    onRetry?.let {
        snackbar.setAction(R.string.retry) { onRetry() }
    }
    snackbar.show()
}

fun View.onClicked(action: () -> Unit) {
    this.setOnClickListener { action() }
}

fun String.asObservable(onChange: (newValue: String) -> Unit) =
    Delegates.observable(this) { _, _, new ->
        onChange(new)
    }