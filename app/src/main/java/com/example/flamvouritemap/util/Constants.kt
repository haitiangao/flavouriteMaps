package com.example.flamvouritemap.util

class Constants {
    companion object{
        //Error Messages
        const val TAG = "TAG_H"
        const val ERROR_PREFIX = "Error: "
        const val RESULTS_NULL = "Results were null"

        //Retrofit call
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=restaurant&keyword=cruise&key=AIzaSyASLA6dWDosNUkXa8Or1uhvWptsahsCCtY
        const val API_KEY = "AIzaSyCKKpRE3W6n9Z6hlpvbESp8sGbuTPpwcGU"
        const val GET_URL_POSTFIX = "maps/api/place/nearbysearch/json"
        const val GET_URL_POSTFIX2 = "/maps/api/place/details/json"
        const val RADIUS = 1500
        const val BASE_URL = "https://maps.googleapis.com/"
    }

}