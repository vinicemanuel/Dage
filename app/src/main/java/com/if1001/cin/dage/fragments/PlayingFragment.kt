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
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.if1001.cin.dage.R
import com.if1001.cin.dage.REQUEST_ID_MULTIPLE_PERMISSIONS
import com.if1001.cin.dage.model.PlayList
import kotlinx.android.synthetic.main.fragment_playing.view.*
import java.util.*

class PlayingFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var playList: PlayList
    private var mMap: GoogleMap? = null
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder
    private lateinit var mapView: MapView

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        try {
            this.mMap?.isMyLocationEnabled = true
            this.mMap?.uiSettings?.isZoomGesturesEnabled = false
        } catch (e: SecurityException) {
            Log.d("Permission: ", "negando permissão")
        }
    }

    override fun onLocationChanged(location: Location) {
        val myPlace = LatLng(location.latitude, location.longitude)

        this.mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 18.0f))

        Log.d("location: ", "update location " + location.toString())
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
        this.myView = inflater.inflate(R.layout.fragment_playing, container, false)
        this.recyclerView = this.myView.recycle_music_list

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager

        this.mapView = this.myView.mapView_music_list

        this.requestUserPermissions()

        return this.myView
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

    private fun configMap() {
        this.mapView.onCreate(null)
        this.mapView.onResume()
        this.mapView.getMapAsync(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
