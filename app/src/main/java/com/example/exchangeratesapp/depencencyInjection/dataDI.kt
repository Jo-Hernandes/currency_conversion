package com.example.exchangeratesapp.depencencyInjection

import com.example.database.DataBaseModule
import com.example.exchangeratesapp.dataSource.DataSource
import com.example.exchangeratesapp.dataSource.DataSourceImpl
import org.koin.dsl.module

val dataSource = module {

    factory<DataSource> {
        return@factory DataSourceImpl(DataBaseModule.getRestServiceRepository(get()))
    }

}



