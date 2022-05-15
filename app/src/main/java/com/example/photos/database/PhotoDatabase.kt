package com.example.photos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabasePhoto::class], version = 1)
abstract class PhotoDatabase: RoomDatabase() {
    abstract val photoDao: PhotoDao
}

private lateinit var INSTANCE: PhotoDatabase

fun getDatabase(context: Context): PhotoDatabase {
    synchronized(PhotoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PhotoDatabase::class.java,
                "photos").build()
        }
    }
    return INSTANCE
}
