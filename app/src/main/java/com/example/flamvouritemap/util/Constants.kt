package com.example.flamvouritemap.util

class Constants {
    companion object{
        //Error Messages
        const val TAG = "TAG_H"
        const val ERROR_PREFIX = "Error: "
        const val RESULTS_NULL = "Results were null"

        //Retrofit call
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyCC5rZMCGMuYOukgRXy2fr5VyD8fJ3LFhE
        const val API_KEY = "AIzaSyCC5rZMCGMuYOukgRXy2fr5VyD8fJ3LFhE"
        const val GET_URL_POSTFIX = "maps/api/place/nearbysearch/json"
        const val RADIUS = 1500
        const val BASE_URL = "https://maps.googleapis.com/"
    }

}