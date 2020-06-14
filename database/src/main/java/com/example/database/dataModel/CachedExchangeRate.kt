package com.example.database.dataModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.nextUp
import kotlin.random.Random

@Entity(tableName = "cachedRate")
data class CachedExchangeRate(
    @ColumnInfo(name = "source_code")
    val sourceCode: String,
    @ColumnInfo(name = "exchange_code")
    val exchangeCode: String,
    @ColumnInfo
    val rate: Double,
    @ColumnInfo
    @PrimaryKey
    val timestamp: Double = Math.random().nextUp()
)