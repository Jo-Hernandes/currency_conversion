package com.example.database.dataModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cachedExchange")
class CachedExchangeCode (

    @ColumnInfo(name = "date_timestamp")
    val timestamp : Long,

    @ColumnInfo(name = "exchange_code")
    @PrimaryKey
    val code : String
)