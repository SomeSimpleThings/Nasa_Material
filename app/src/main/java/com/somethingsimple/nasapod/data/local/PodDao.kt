package com.somethingsimple.nasapod.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.somethingsimple.nasapod.data.PictureOfDay

@Dao
interface PodDao {
    @Query("SELECT * FROM PictureOfDay")
    fun getAll(): List<PictureOfDay>

    @Insert
    fun insertAll(vararg pictureOfDay: PictureOfDay)

    @Delete
    fun delete(pictureOfDay: PictureOfDay)
}