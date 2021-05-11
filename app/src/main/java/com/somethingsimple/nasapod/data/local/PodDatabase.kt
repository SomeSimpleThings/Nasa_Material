package com.somethingsimple.nasapod.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.somethingsimple.nasapod.data.PictureOfDay

@Database(entities = [PictureOfDay::class], version = 1)
abstract class PodDatabase : RoomDatabase() {
    abstract fun podDao(): PodDao
}