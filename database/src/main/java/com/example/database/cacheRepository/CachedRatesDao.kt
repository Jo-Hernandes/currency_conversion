package com.example.database.cacheRepository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.dataModel.CachedExchangeRate
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CachedRatesDao {

    @Query("SELECT * FROM cachedRate WHERE source_code = :code")
    fun getRate(code : String): Single<List<CachedExchangeRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(model: CachedExchangeRate): Completable

    @Query("DELETE FROM cachedRate")
    fun clearCachedData() : Completable

}