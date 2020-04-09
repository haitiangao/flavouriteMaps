package com.example.flamvouritemap.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flamvouritemap.database.FavouriteRepository
import com.example.flamvouritemap.firebase.FirebaseEvents
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.model.Library
import com.example.flamvouritemap.network.MapRetrofitService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapViewModel(application: Application): AndroidViewModel(application) {
    private val mapRetrofitService: MapRetrofitService = MapRetrofitService()
    private var firebaseEvent: FirebaseEvents = FirebaseEvents()
    private var favouriteRepository: FavouriteRepository =FavouriteRepository(application)


    fun getMapsRx(location: String, type: String): Observable<Library> {
        return mapRetrofitService
            .getMapsRx(location,type)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getSpecificPlaceRx(placeid: String): Observable<com.example.flamvouritemap.placeModel.Library>{
        return mapRetrofitService
            .getSpecificPlaceRx(placeid)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getAllFavourites():MutableList<FavouriteResult>{
        return favouriteRepository.allFavourites
    }

    fun sendNewFavourite(favouriteResult: FavouriteResult) {
        favouriteRepository.insertAFavourite(favouriteResult)
    }

    fun removeFavourite(favouriteResult: FavouriteResult) {
        favouriteRepository.deleteAFavourite(favouriteResult)
    }
    fun disposeDisposables(){
        favouriteRepository.disposeDisposables()
    }



}