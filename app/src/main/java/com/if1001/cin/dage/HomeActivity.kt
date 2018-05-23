package com.if1001.cin.dage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.home_activity.*
import java.util.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {


    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder

    override fun onLocationChanged(location: Location) {
        Log.d("location: ", "update location " + location.toString())
        val cord = "(${location.latitude.format(2)}, ${location.longitude.format(2)})"
        this.gpsCoordinates.text = cord

        val addresses = this.mGeocoder.getFromLocation(location.latitude,location.longitude,1)

        val city = addresses[0].locality
        val address = addresses[0].getAddressLine(0)
        val fullAddress = "$city, $address"
        this.userLocation.text = fullAddress
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        this.requestUserPermissions()
    }

    private fun requestUserPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ID_MULTIPLE_PERMISSIONS)
        }else{
            this.mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 1f, this)
            this.mGeocoder = Geocoder(applicationContext, Locale.getDefault())
            this.configMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            this.REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission: ", "Permission has been denied by user")
                    this.requestUserPermissions()
                } else {
                    Log.d("Permission: ", "Permission has been granted by user")
                    this.configMap()
                }
            }
        }
    }

    private fun configMap(){
        this.mapView.onCreate(null)
        this.mapView.onResume()
        this.mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap
        try {
            this.mMap.isMyLocationEnabled = true
        }catch (e: SecurityException){
            Log.d("Permission: ", "negando permissão")
        }
    }
}
