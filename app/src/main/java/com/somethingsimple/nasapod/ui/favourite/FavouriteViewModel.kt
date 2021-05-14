package com.somethingsimple.nasapod.ui.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.somethingsimple.nasapod.data.PictureOfDay
import com.somethingsimple.nasapod.data.PodRepo

class FavouriteViewModel(
    val favouritesLiveData: MutableLiveData<List<PictureOfDay>> = MutableLiveData<List<PictureOfDay>>(),
    val podRepo: PodRepo = PodRepo()
) : ViewModel() {

    fun getAllFavourites(): MutableLiveData<List<PictureOfDay>> {
        favouritesLiveData.value = podRepo.getFavourites()
        return favouritesLiveData
    }

    fun removeFromFavourites(pictureOfDay: PictureOfDay) {
        podRepo.deleteFromFavourites(pictureOfDay)
    }

    fun setFavourite(pictureOfDay: PictureOfDay) {
        if (pictureOfDay.liked)
            podRepo.addToFavourites(pictureOfDay)
        else podRepo.deleteFromFavourites(pictureOfDay)
    }
}