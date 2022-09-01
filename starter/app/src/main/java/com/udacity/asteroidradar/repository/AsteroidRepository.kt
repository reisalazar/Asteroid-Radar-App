package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.api.Network.getPictureOfTheDay
import com.udacity.asteroidradar.api.Network.retrofitService
import com.udacity.asteroidradar.database.AsteroidDatabaseRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabaseRoom) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val result = retrofitService.getAsteroids("", "")
            val resultJsonObject =
                parseAsteroidsJsonResult(JSONObject(result))
            database.asteroidDao.insertAll(resultJsonObject.asDatabaseModel())
        }
    }
    suspend fun getPictureOfDay(): PictureOfDay {
        lateinit var pictureOfTheDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfTheDay = getPictureOfTheDay()
        }
        return pictureOfTheDay
    }
}
