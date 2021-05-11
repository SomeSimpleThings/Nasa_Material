package com.somethingsimple.nasapod.data

import com.somethingsimple.nasapod.BuildConfig
import com.somethingsimple.nasapod.NasaPodApplication
import com.somethingsimple.nasapod.data.local.PodDao
import com.somethingsimple.nasapod.data.remote.PODRetrofitImpl
import com.somethingsimple.nasapod.data.remote.PictureOfTheDayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PodRepo(
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl(),
    private val podDao: PodDao = NasaPodApplication.getPodDao(),
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    )
) {

    fun addToFavourites(pictureOfDay: PictureOfDay) {
        podDao.insertAll(pictureOfDay)
    }

    fun deleteFromFavourites(pictureOfDay: PictureOfDay) {
        podDao.delete(pictureOfDay)
    }

    fun getFavourites(): List<PictureOfDay> {
        return podDao.getAll()
    }

    fun getToday(completion: (PictureOfTheDayResponse?) -> Unit) {
        val calendar = Calendar.getInstance()
        getOnDay(simpleDateFormat.format(calendar.time).toString(), completion)
    }

    fun getYesterday(completion: (PictureOfTheDayResponse?) -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        getOnDay(simpleDateFormat.format(calendar.time).toString(), completion)
    }

    private fun getOnDay(dateTime: String, completion: (PictureOfTheDayResponse?) -> Unit) {
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            completion(PictureOfTheDayResponse.Error(Throwable("You need API key")))
        } else {
            retrofitImpl.getRetrofitImpl().getConcreteDayPod(apiKey, dateTime).enqueue(object :
                Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.apply {
                            val cached = url?.let { podDao.getByUrl(it) }
                            if (cached != null) {
                                cached.liked = true
                                completion(PictureOfTheDayResponse.Success(cached))
                            } else
                                completion(PictureOfTheDayResponse.Success(this))
                        }

                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            completion(PictureOfTheDayResponse.Error(Throwable("Unidentified error")))
                        } else {
                            completion(PictureOfTheDayResponse.Error(Throwable(message)))
                        }
                    }
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    completion(PictureOfTheDayResponse.Error(t))
                }
            })
        }
    }

    fun getRandom(completion: (PictureOfTheDayResponse?) -> Unit) {
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            completion(PictureOfTheDayResponse.Error(Throwable("You need API key")))
        } else {
            retrofitImpl.getRetrofitImpl().getRandomPods(apiKey).enqueue(object :
                Callback<List<PictureOfDay>> {
                override fun onResponse(
                    call: Call<List<PictureOfDay>>,
                    response: Response<List<PictureOfDay>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        completion(PictureOfTheDayResponse.Success(response.body()!![0]))
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            completion(PictureOfTheDayResponse.Error(Throwable("Unidentified error")))
                        } else {
                            completion(PictureOfTheDayResponse.Error(Throwable(message)))
                        }
                    }
                }

                override fun onFailure(call: Call<List<PictureOfDay>>, t: Throwable) {
                    completion(PictureOfTheDayResponse.Error(t))
                }
            })
        }
    }
}