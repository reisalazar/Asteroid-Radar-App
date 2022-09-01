package com.udacity.asteroidradar

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabaseRoom
import com.udacity.asteroidradar.database.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AsteroidDatabaseRoom.getDatabaseInstance(app)
    private val repository = AsteroidRepository(database)

    val asteroids = repository.asteroids

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val navigateToItemDetail = MutableLiveData<Asteroid>()

    init {
        refreshAsteroids()
        getPictureOfTheDay()

    }

    fun navigateToDetail(item: Asteroid) {
        navigateToItemDetail.value = item
    }

    fun detailNavigated() {
        navigateToItemDetail.value = null
    }

    private fun getPictureOfTheDay() = viewModelScope.launch(Dispatchers.Main) {
        try {
            _pictureOfTheDay.value = repository.getPictureOfDay()
        }catch (exception: Exception) {
            Log.e("PictureOfTheDayError", exception.stackTraceToString())
        }

    }

    private fun refreshAsteroids() = viewModelScope.launch(Dispatchers.IO) {
       try {
           repository.refreshAsteroids()
       }catch (exception : Exception) {
           Log.e("RefreshAsteroidsError", exception.stackTraceToString())
       }
    }
    private val asteroidLItemList = MutableLiveData(AsteroidApiFilter.SHOW_WEEK)


    val asteroidsList = Transformations.switchMap(asteroidLItemList) {
        when (it) {
            AsteroidApiFilter.SHOW_TODAY -> repository.asteroidsOfToday
            AsteroidApiFilter.SHOW_WEEK -> repository.asteroidsOfWeek
            else -> {
                repository.asteroids
            }
        }
    }

    fun selectFilter(filter: AsteroidApiFilter) {
        asteroidLItemList.value = filter
    }
}