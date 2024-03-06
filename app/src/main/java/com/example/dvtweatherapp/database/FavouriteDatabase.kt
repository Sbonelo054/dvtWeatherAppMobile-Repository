package com.example.dvtweatherapp.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FavouriteTable::class, OfflineTable::class, OfflineForecastTable::class],
    version = 3
)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        @Synchronized
        fun getInstance(context: Application): FavouriteDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    FavouriteDatabase::class.java,
                    "favourite_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build()
                INSTANCE = instance
            }
            return instance
        }

        private val callback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}