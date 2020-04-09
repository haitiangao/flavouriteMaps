package com.example.flamvouritemap.network

import com.example.flamvouritemap.model.Library
import com.example.flamvouritemap.model.Result
import com.example.flamvouritemap.util.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyCC5rZMCGMuYOukgRXy2fr5VyD8fJ3LFhE
    @GET(Constants.GET_URL_POSTFIX)
    fun getMapRx(@Query("location") location: String, @Query("radius") radius: Int,
                   @Query("type") type: String,
                   @Query("key") key: String): Observable<Library>


    //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyCC5rZMCGMuYOukgRXy2fr5VyD8fJ3LFhE
    @GET(Constants.GET_URL_POSTFIX2)
    fun getSpecificPlaceRx(@Query("placeid") placeid: String, @Query("key") key: String): Observable<com.example.flamvouritemap.placeModel.Library>
}