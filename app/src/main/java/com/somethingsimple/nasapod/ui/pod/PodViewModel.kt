package com.somethingsimple.nasapod.ui.pod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.nasapod.data.PictureOfDay
import com.somethingsimple.nasapod.data.PodRepo
import com.somethingsimple.nasapod.data.remote.PictureOfTheDayResponse

class PodViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayResponse> = MutableLiveData(),
    private val podRepo: PodRepo = PodRepo()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayResponse> {
        getToday()
        return liveDataForViewToObserve
    }

    fun getToday() {
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

    fun setFavourite(pictureOfDay: PictureOfDay) {
        if (pictureOfDay.liked)
            podRepo.addToFavourites(pictureOfDay)
        else podRepo.deleteFromFavourites(pictureOfDay)
    }
}
