package com.if1001.cin.dage

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    private lateinit var mMap: GoogleMap

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
        Log.d("Permission: ", "deve carregar o mapa aqui")
        var myMapStyle = "https://maps.googleapis.com/maps/api/staticmap?key=${R.string.google_maps_key}&center=-8.0610057,-34.8712797&zoom=12&format=png&maptype=roadmap&size=480x360"
        var style = MapStyleOptions(myMapStyle)
        this.mMap.setMapStyle(style)
    }
}
