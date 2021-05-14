package com.somethingsimple.nasapod.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PictureOfDay(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo
    @SerializedName("date")
    val date: String,
    @ColumnInfo
    @SerializedName("explanation")
    val explanation: String,
    @ColumnInfo
    @SerializedName("hdurl")
    val hdurl: String?,
    @ColumnInfo
    @SerializedName("media_type")
    val mediaType: String,
    @ColumnInfo
    @SerializedName("service_version")
    val serviceVersion: String,
    @ColumnInfo
    @SerializedName("title")
    val title: String,
    @ColumnInfo
    @SerializedName("url")
    val url: String?,
    var liked: Boolean = false
)