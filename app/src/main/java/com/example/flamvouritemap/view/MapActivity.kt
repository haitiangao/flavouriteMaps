package com.example.flamvouritemap.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.example.flamvouritemap.R
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.model.Library
import com.example.flamvouritemap.util.DebugLogger
import com.example.flamvouritemap.viewmodel.MapViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList
import com.example.flamvouritemap.model.Result


class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var locationManager: LocationManager
    //private val TAG: String = MapsFragment::class.java.getSimpleName()
    private val REQUEST_LOCATION_PERMISSION = 1
    private val INITIAL_ZOOM = 8f
    private var username:String? = null
    private var compositeDisposable:CompositeDisposable = CompositeDisposable()
    private var poiList: MutableList<Result> = ArrayList<Result>()

    private lateinit var mMap: GoogleMap
    private lateinit var  favouriteFragment:FavouriteFragment
    private lateinit var mapViewModel: MapViewModel
    private lateinit var latLng: LatLng

    @BindView(R.id.favouriteFrame)
    lateinit var favouriteFrame: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        username = intent.getStringExtra("Username")
        DebugLogger.logDebug(username.toString())
        mapViewModel  = ViewModelProvider(this).get<MapViewModel>(MapViewModel::class.java)

        setUpLocation()
        favouriteFragment= FavouriteFragment(username)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.favouriteFrame,favouriteFragment)
            .commit()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    private fun gettingPoi(){

        val stringLatLng = ""+latLng.latitude+","+latLng.longitude
        DebugLogger.logDebug("showing string lat lng: $stringLatLng")

        compositeDisposable.add(mapViewModel.getMapsRx(stringLatLng,"restaurant")
            .subscribe({ results->
                DebugLogger.logDebug("Getting rx")
                setupPoi(results)
        },{throwable ->
                DebugLogger.logError(throwable)
            }))
    }

    private fun setupPoi(results: Library) {
        poiList = results.results
        DebugLogger.logDebug("Getting results")
        DebugLogger.logDebug("Getting element:" +poiList[0].name)

        for (element in poiList){
            DebugLogger.logDebug("Getting element:" +element.name)
            addLocationMarker(element.name, element.geometry.location.lat,
                element.geometry.location.lng)
        }

    }

    private fun addLocationMarker( name:String, latitude:Double, longitude:Double) {
        val poiLatLng = LatLng(latitude,longitude)
        mMap.addMarker(MarkerOptions()
            .position(poiLatLng)
            .title(name))
    }


    @SuppressLint("MissingPermission")
    private fun setUpLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            10f,
            this
        )

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latLng = LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        DebugLogger.logDebug("Onready: "+ latLng)
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                INITIAL_ZOOM
            )
        )
        val homeOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
            .position(latLng, 100f)
        mMap.addGroundOverlay(homeOverlay)

        setPoiClick(mMap) // Set a click listener for points of interest.
        enableMyLocation(mMap) // Enable location tracking.
        gettingPoi()
        setOnMarkerClick(mMap)
    }

    private fun setOnMarkerClick(map:GoogleMap){
        map.setOnMarkerClickListener { marker ->
            for(element in poiList)
            {
                if(element.geometry.location.lat==marker.position.latitude
                    &&element.geometry.location.lng==marker.position.longitude) {
                    val favouriteResult = FavouriteResult(element.placeId)
                    mapViewModel.sendNewFavourite(favouriteResult)
                    favouriteFragment.addToView(element.placeId)
                }
            }
            false
        }
    }


    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
            poiMarker.tag = getString(R.string.poi)

        }
    }


    /**
     * Checks for location permissions, and requests them if they are missing.
     * Otherwise, enables the location layer.
     */
    private fun enableMyLocation(map: GoogleMap) {
        if (let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        mapViewModel.disposeDisposables()

    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            DebugLogger.logDebug("LOCATION : " + location.latitude + "," + location.longitude)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

}
