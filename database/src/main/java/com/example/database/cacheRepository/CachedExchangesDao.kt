package com.example.database.cacheRepository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.dataModel.CachedExchangeCode
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CachedExchangesDao {

    @Query("SELECT * FROM cachedExchange")
    fun getRate(): Single<List<CachedExchangeCode?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(model: CachedExchangeCode): Completable

    @Query("DELETE FROM cachedExchange")
    fun clearCachedData() : Completable

}