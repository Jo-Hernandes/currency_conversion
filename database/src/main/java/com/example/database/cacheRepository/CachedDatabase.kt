package com.example.database.cacheRepository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.dataModel.CachedExchangeCode
import com.example.database.dataModel.CachedExchangeRate

@Database(entities = [CachedExchangeCode::class, CachedExchangeRate::class], version = 2)
abstract class CachedDatabase : RoomDatabase() {

    abstract fun getExchangesDao(): CachedExchangesDao
    abstract fun getRatesDao(): CachedRatesDao

    companion object{

        @Volatile private var INSTANCE: CachedDatabase? = null

        fun getInstance(context: Context): CachedDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CachedDatabase::class.java, "CachedItemsDatabase.db")
                .build()
    }
}