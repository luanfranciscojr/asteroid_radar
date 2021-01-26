package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

enum class AsteroidFilter {
    WEEK,
    TODAY,
    SAVED
}

enum class ApiStatus { LOADING, ERROR, DONE }

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

interface FeedService {

    @GET("neo/rest/v1/feed")
    suspend fun getNearEarthObjects(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("api_key") apiKey: String = Constants.API_KEY
    ): NearEarthObjects

}

interface PictureOfDayService {

    @GET("planetary/apod")
    suspend fun getPictureOfDay(
            @Query("api_key") apiKey: String = Constants.API_KEY
    ): PictureOfDayDto

}

object AsteroidApi {

    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val feedService: FeedService by lazy {
        retrofit.create(FeedService::class.java)
    }

    val pictureOfDayService: PictureOfDayService by lazy {
        retrofit.create(PictureOfDayService::class.java)
    }
}
