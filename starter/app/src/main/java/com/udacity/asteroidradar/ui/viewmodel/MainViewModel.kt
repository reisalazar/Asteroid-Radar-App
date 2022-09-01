package com.udacity.asteroidradar.ui.viewmodel

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

    val itemDetail = MutableLiveData<Asteroid?>()
    val asteroids = repository.asteroids

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    init {
        refreshAsteroids()
        getPictureOfTheDay()

    }
    fun showDetail(item: Asteroid) {
        itemDetail.value = item
    }
    fun detailShown() {
        itemDetail.value = null
    }

    private fun getPictureOfTheDay() = viewModelScope.launch(Dispatchers.IO) {
        _pictureOfTheDay.value = repository.getPictureOfDay()
    }

    private fun refreshAsteroids() = viewModelScope.launch(Dispatchers.IO) {
        repository.refreshAsteroids()
    }

}