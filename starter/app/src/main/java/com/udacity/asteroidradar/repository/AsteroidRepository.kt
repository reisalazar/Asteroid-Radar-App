package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Utils.Companion.getDaysTo
import com.udacity.asteroidradar.Utils.Companion.getTodayDate
import com.udacity.asteroidradar.api.Network.getPictureOfTheDay
import com.udacity.asteroidradar.api.Network.retrofitService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabaseRoom
import com.udacity.asteroidradar.database.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabaseRoom) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    val asteroidsOfToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.
        getTodayAsteroids(Filter.TODAY.startDate)) {
            it.asDomainModel()
        }

    val asteroidsOfWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.
        getWeekAsteroids(Filter.WEEK.startDate, Filter.WEEK.endDate)) {
            it.asDomainModel()
        }


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

enum class Filter(val startDate: String, val endDate: String) {
    TODAY(getTodayDate(), getTodayDate()),
    WEEK(getTodayDate(), getDaysTo(7))
}
