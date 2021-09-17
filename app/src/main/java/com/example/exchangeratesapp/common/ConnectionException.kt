package com.example.exchangeratesapp.common

import androidx.annotation.StringRes
import com.example.exchangeratesapp.R

sealed class ConnectionException(@StringRes val message: Int, val retry: () -> Unit) {

    class FetchDataException(retry: () -> Unit) :
        ConnectionException(R.string.fetch_exception, retry)

    class OfflineException(retry: () -> Unit) :
        ConnectionException(R.string.offline_exception, retry)

    class NoHostException(retry: () -> Unit) :
        ConnectionException(R.string.no_host, retry)

    class RatesNotLoaded(retry: () -> Unit) :
            ConnectionException(R.string.no_rates, retry)

}
