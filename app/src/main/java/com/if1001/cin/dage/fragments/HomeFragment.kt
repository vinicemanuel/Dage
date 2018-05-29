package com.if1001.cin.dage.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.if1001.cin.dage.R
import com.if1001.cin.dage.format
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener {

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder
    private lateinit var myView: View
    private lateinit var mapView: MapView
    private lateinit var gpsCoordinates: TextView
    private lateinit var userLocation: TextView

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_home, container, false)

        this.mapView = myView.findViewById(R.id.mapView)
        this.gpsCoordinates = myView.findViewById(R.id.gpsCoordinates)
        this.userLocation = myView.findViewById(R.id.userLocation)

        this.requestUserPermissions()
        return this.myView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun requestUserPermissions(){
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ID_MULTIPLE_PERMISSIONS)
        }else{
            this.mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 1f, this)
            this.mGeocoder = Geocoder(activity?.applicationContext, Locale.getDefault())
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
