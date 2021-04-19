package com.somethingsimple.nasapod.data

import com.google.gson.GsonBuilder
import com.somethingsimple.nasapod.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException


const val API_APOD = "planetary/apod"
const val API_QUERYPARAM_APIKEY = "api_key"
const val API_QUERYPARAM_THUMBS = "thumbs"
const val API_QUERYPARAM_COUNT = "count"
const val API_QUERYPARAM_DATE = "date"

interface PictureOfTheDayAPI {

    @GET(API_APOD)
    fun getPictureOfTheDay(
        @Query(API_QUERYPARAM_APIKEY) apiKey: String,
        @Query(API_QUERYPARAM_THUMBS) thumbs: Boolean = true
    ): Call<PictureOfDay>

    @GET(API_APOD)
    fun getRandomPods(
        @Query(API_QUERYPARAM_APIKEY) apiKey: String,
        @Query(API_QUERYPARAM_COUNT) count: Int = 1,
    ): Call<PictureOfDay>

    @GET(API_APOD)
    fun getConcreteDayPod(
        @Query(API_QUERYPARAM_APIKEY) apiKey: String,
        @Query(API_QUERYPARAM_DATE) date: String,
        @Query(API_QUERYPARAM_THUMBS) thumbs: Boolean = true
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