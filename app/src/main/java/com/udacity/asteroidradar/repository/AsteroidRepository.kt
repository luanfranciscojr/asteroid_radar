package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.common.getLatterDate
import com.udacity.asteroidradar.common.parserToString
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    val pictureOfDay: LiveData<PictureOfDay> = Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it.asDomainModel()
    }

    fun getAsteroids(asteroidFilter: AsteroidFilter) =  when (asteroidFilter) {
        AsteroidFilter.WEEK -> database.asteroidDao.getAsteroids(Date(), getLatterDate(7))
        AsteroidFilter.TODAY -> database.asteroidDao.getAsteroids(Date(), Date())
        else -> database.asteroidDao.getAllAsteroids()
    }.map { it.asDomainModel() }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            refreshAsteroids(Date(),getLatterDate(7))
            refreshPictureOfDay();
        }
    }
    suspend fun refreshAsteroids(from: Date, to: Date) {
        val playlist = AsteroidApi.feedService.getNearEarthObjects(from.parserToString(), to.parserToString())
        database.asteroidDao.insertAll(*playlist.asDatabaseModel())
    }

    suspend fun refreshPictureOfDay() {
        val pcitureOfDay = AsteroidApi.pictureOfDayService.getPictureOfDay()
        database.pictureOfDayDao.insert(pcitureOfDay.asDatabaseModel());
    }

    suspend fun deletePreviousAsteroids() {
        withContext(Dispatchers.IO) {
            val previousDay = getLatterDate(-1)
            val previousAsteroids = database.asteroidDao.getAsteroids(previousDay, previousDay)
            previousAsteroids.value?.let { asteroids ->
                database.asteroidDao.deleteByIds(asteroids.map { asteroid -> asteroid.id })
            }
        }
    }

}