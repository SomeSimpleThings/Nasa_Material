package com.somethingsimple.nasapod.ui.pod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.nasapod.data.PictureOfTheDayResponse
import com.somethingsimple.nasapod.data.PodRepo

class PodViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayResponse> = MutableLiveData(),
    private val podRepo: PodRepo = PodRepo()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayResponse> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheDayResponse.Loading(null)
        podRepo.getToday {
            liveDataForViewToObserve.value = it
        }
    }


    fun getRandom() {
        liveDataForViewToObserve.value = PictureOfTheDayResponse.Loading(null)
        podRepo.getRandom {
            liveDataForViewToObserve.value = it
        }
    }

    fun getYesterday() {
        liveDataForViewToObserve.value = PictureOfTheDayResponse.Loading(null)
        podRepo.getYesterday {
            liveDataForViewToObserve.value = it
        }
    }
}
