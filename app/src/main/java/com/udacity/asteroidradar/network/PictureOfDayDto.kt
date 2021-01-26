package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.PictureOfDayEntity

@JsonClass(generateAdapter = true)
data class PictureOfDayDto(
        @Json(name = "url") val url: String,
        @Json(name = "media_type") val mediaType: String,
        @Json(name = "title") val title: String
)

fun PictureOfDayDto.asDatabaseModel(): PictureOfDayEntity =
            PictureOfDayEntity(
                   url = url,
                   mediaType = mediaType,
                   title = title
            )
