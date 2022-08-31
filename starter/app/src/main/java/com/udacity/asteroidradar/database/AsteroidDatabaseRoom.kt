package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids:AsteroidEntity)

    @Update
    fun update(asteroid: AsteroidEntity)

    @Query("select * from asteroids_table")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids_table")
    fun deleteAll()
}

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabaseRoom : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    private lateinit var INSTANCE: AsteroidDatabaseRoom

    fun getDatabase(context: Context): AsteroidDatabaseRoom {
        synchronized(AsteroidDatabaseRoom::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabaseRoom::class.java,
                    "asteroids_database"
                ).build()
            }
        }
        return INSTANCE
    }
}

