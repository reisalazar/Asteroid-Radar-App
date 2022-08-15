package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {


    @Insert
    suspend fun insertAll(asteroids: List<Asteroid>)

    @Update
    fun update(asteroid: com.udacity.asteroidradar.Asteroid)

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= date() ORDER BY close_approach_date")
    fun getAllAsteroids(): List<com.udacity.asteroidradar.Asteroid>

    @Query("DELETE FROM asteroids_table")
    fun deleteAll()


}