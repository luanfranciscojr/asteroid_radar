package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "picture_of_day")
data class PictureOfDayEntity constructor(

        @PrimaryKey
        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "url")
        val url: String,

        @ColumnInfo(name = "media_type")
        val mediaType: String

)

fun PictureOfDayEntity.asDomainModel(): PictureOfDay =
    PictureOfDay(
            mediaType,
            title,
            url
    )