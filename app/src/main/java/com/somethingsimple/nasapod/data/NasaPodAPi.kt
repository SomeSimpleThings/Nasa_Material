package com.somethingsimple.nasapod.data

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

private const val BASE_URL = "https://api.nasa.gov/"

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<PictureOfDay>

    @GET("planetary/apod")
    fun getRandomPods(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int = 1,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<PictureOfDay>

    @GET("planetary/apod")
    fun getConcreteDayPod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<PictureOfDay>
}

class PODRetrofitImpl {

    fun getRetrofitImpl(): PictureOfTheDayAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return podRetrofit.create(PictureOfTheDayAPI::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}

sealed class PictureOfTheDayResponse {
    data class Success(val serverResponse: PictureOfDay) : PictureOfTheDayResponse()
    data class Error(val error: Throwable) : PictureOfTheDayResponse()
    data class Loading(val progress: Int?) : PictureOfTheDayResponse()
}