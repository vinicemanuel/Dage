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
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.if1001.cin.dage.MAP_PLAY_LIST_FRAGMENT_TAG
import com.if1001.cin.dage.R
import com.if1001.cin.dage.REQUEST_ID_MULTIPLE_PERMISSIONS
import com.if1001.cin.dage.format
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private var mMap: GoogleMap? = null
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder
    private lateinit var myView: View
    private lateinit var mapView: MapView
    private lateinit var gpsCoordinates: TextView
    private lateinit var userLocation: TextView
    private lateinit var initButton: FloatingActionButton
    private lateinit var userToken: String
    private lateinit var userId: String

    private lateinit var mapPlaylistFragment: MapPlaylistFragment

    override fun onLocationChanged(location: Location) {

        val myPlace = LatLng(location.latitude, location.longitude)

        this.mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 18.0f))

        Log.d("location: ", "update location " + location.toString())
        val cord = "(${location.latitude.format(2)}, ${location.longitude.format(2)})"
        this.gpsCoordinates.text = cord

        val addresses = this.mGeocoder.getFromLocation(location.latitude, location.longitude, 1)

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
        // get token from bundle
        this.userToken = arguments!!.getString("userToken")
        this.userId = arguments!!.getString("userId")

        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_home, container, false)

        this.mapView = this.myView.mapView
        this.gpsCoordinates = this.myView.gpsCoordinates
        this.userLocation = this.myView.userLocation
        this.initButton = this.myView.playButton

        this.mapPlaylistFragment = MapPlaylistFragment()
        val bundle = Bundle()
        bundle.putString("userToken", userToken)
        bundle.putString("userId", userId)
        mapPlaylistFragment.arguments = bundle

        this.initButton.setOnClickListener {
            val fragment = fragmentManager!!.findFragmentByTag(MAP_PLAY_LIST_FRAGMENT_TAG)
            if (fragment == null) {
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.mapPlaylistFragment, MAP_PLAY_LIST_FRAGMENT_TAG).commit()
            } else if (fragment.isHidden) {
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.mapPlaylistFragment, MAP_PLAY_LIST_FRAGMENT_TAG).commit()
            }
        }

        this.requestUserPermissions()
        return this.myView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun requestUserPermissions() {
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ID_MULTIPLE_PERMISSIONS)
        } else {
            this.mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 1f, this)
            this.mGeocoder = Geocoder(activity?.applicationContext, Locale.getDefault())
            this.configMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
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

    private fun configMap() {
        this.mapView.onCreate(null)
        this.mapView.onResume()
        this.mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap
        try {
            this.mMap?.isMyLocationEnabled = true
            this.mMap?.uiSettings?.isZoomGesturesEnabled = false
        } catch (e: SecurityException) {
            Log.d("Permission: ", "negando permissão")
        }
    }
}
