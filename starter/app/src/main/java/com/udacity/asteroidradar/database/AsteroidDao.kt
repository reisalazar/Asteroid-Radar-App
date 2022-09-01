package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids_table ORDER BY close_approach_date ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( asteroids: List<AsteroidEntity>)

    @Update
    fun update(asteroid: AsteroidEntity)

    @Query("SELECT * FROM asteroids_table")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids_table")
    fun deleteAll()
}