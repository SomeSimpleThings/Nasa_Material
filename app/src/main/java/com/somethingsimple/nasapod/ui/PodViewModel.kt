package com.somethingsimple.nasapod.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.nasapod.BuildConfig
import com.somethingsimple.nasapod.data.PODRetrofitImpl
import com.somethingsimple.nasapod.data.PictureOfDay
import com.somethingsimple.nasapod.data.PictureOfTheDayResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PodViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayResponse> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayResponse> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheDayResponse.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayResponse.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayResponse.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayResponse.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayResponse.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayResponse.Error(t)
                }
            })
        }
    }
}
