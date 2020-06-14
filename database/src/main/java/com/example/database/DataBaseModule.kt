package com.example.database

import android.content.Context
import com.example.database.restRepository.CurrencyLayerRepository

class DataBaseModule {

    companion object {
        fun getRestServiceRepository(context: Context) : Repository = CurrencyLayerRepository(context)
    }
}