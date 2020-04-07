package com.example.flamvouritemap.network

import com.example.flamvouritemap.model.Library
import com.example.flamvouritemap.model.Result
import com.example.flamvouritemap.util.Constants
import com.example.flamvouritemap.util.Constants.Companion.API_KEY
import com.example.flamvouritemap.util.Constants.Companion.RADIUS
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class MapRetrofitService {
    private val mapService: MapService
    private val client: OkHttpClient


    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createGoogleService(retrofitInstance: Retrofit): MapService {
        return retrofitInstance.create(MapService::class.java)
    }

    fun getMapsRx(location: String, type: String, keyword: String): Observable<Library> {
        return mapService.getMapRx(location, RADIUS, type, keyword, API_KEY)
    }

    fun getSpecificPlaceRx(placeid: String): Observable<Result>{
        return mapService.getSpecificPlaceRx(placeid, API_KEY)
    }



    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        mapService = createGoogleService(getRetrofitInstance())
    }
}