package com.udacity.asteroidradar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabaseRoom
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

    private val _navigateToItemDetail = MutableLiveData<Asteroid>()
    val navigateToItemDetail
        get() = _navigateToItemDetail

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
        _pictureOfTheDay.value = repository.getPictureOfDay()
    }

    private fun refreshAsteroids() = viewModelScope.launch(Dispatchers.IO) {
        repository.refreshAsteroids()
    }

}