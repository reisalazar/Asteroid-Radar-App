package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabaseRoom : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabaseRoom

        fun getDatabaseInstance(context: Context): AsteroidDatabaseRoom {
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
}

