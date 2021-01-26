package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.ApiStatus
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _asteroidFilterSelected = MutableLiveData(AsteroidFilter.WEEK)
    val pictureOfDay = asteroidRepository.pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(_asteroidFilterSelected) {
        asteroidRepository.getAsteroids(it)
    }

    val status =  MutableLiveData<ApiStatus>(ApiStatus.LOADING)

    init {
        refresh();
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                status.value = ApiStatus.LOADING
                asteroidRepository.refresh()
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun openAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun search(asteroidFilter: AsteroidFilter) {
        _asteroidFilterSelected.value = asteroidFilter
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}


