package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import java.util.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: AsteroidEntity)

    @Query("select * from asteroid  WHERE close_approach_date BETWEEN :startDate AND :endDate ")
    fun getAsteroids(startDate: Date, endDate: Date): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("delete from asteroid where id in(:ids)")
    fun deleteByIds(ids: List<Long>)

}