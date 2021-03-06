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
import com.if1001.cin.dage.PLAYING_SONG_FRAGMENT_TAG
import com.if1001.cin.dage.R
import com.if1001.cin.dage.REQUEST_ID_MULTIPLE_PERMISSIONS
import com.if1001.cin.dage.adapters.ContentListenerPlayList
import com.if1001.cin.dage.adapters.PlayListsAdapter
import com.if1001.cin.dage.model.PlayList
import kotlinx.android.synthetic.main.fragment_map_playlist.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Fragment do mapa + lista de playlists
 */
class MapPlaylistFragment : Fragment(), OnMapReadyCallback, LocationListener, ContentListenerPlayList {
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists: List<PlayList>
    private lateinit var userToken: String
    private lateinit var userId: String
    private var mMap: GoogleMap? = null
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder
    private lateinit var mapView: MapView
    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null
    private lateinit var playingFragment: PlayingFragment

    /**
     * Executado no clique (toque) em uma playlist (seleciona a playlist)
     */
    override fun onItemClicked(item: PlayList) {
        Log.d("click", "${item.PlayListName}")

        val fragment = fragmentManager!!.findFragmentByTag(PLAYING_SONG_FRAGMENT_TAG)
        this.playingFragment.playListPlaingName = item.PlayListName
        this.playingFragment.playList = item
        if (fragment == null) {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.playingFragment, PLAYING_SONG_FRAGMENT_TAG).commit()
        } else if (fragment.isHidden) {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.playingFragment, PLAYING_SONG_FRAGMENT_TAG).commit()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        try {
            this.mMap?.isMyLocationEnabled = true
            this.mMap?.uiSettings?.isZoomGesturesEnabled = false
        } catch (e: SecurityException) {
            Log.d("Permission: ", "negando permissão")
        }
    }

    /**
     * Tracking de movimentação do mapa
     */
    override fun onLocationChanged(location: Location) {
        val myPlace = LatLng(location.latitude, location.longitude)

        this.mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 18.0f))

        Log.d("location: ", "update location " + location.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Obtém Tokem passado pelo fragment antecessor
        this.userToken = arguments!!.getString("userToken")
        this.userId = arguments!!.getString("userId")

        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_map_playlist, container, false)
        this.recyclerView = this.myView.recycle_view_playlist

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager

        // get playlists from Spotify
        this.playlists = ArrayList()
        this.recyclerView.adapter = PlayListsAdapter(this.playlists, this.activity!!, this)
        getSpotifyPlaylists(this.playlists, this.recyclerView.adapter)

        this.mapView = this.myView.mapViewPlayList

        this.requestUserPermissions()

        val bundle = Bundle()
        bundle.putString("userId", userId)
        bundle.putString("userToken", userToken)
        this.playingFragment = PlayingFragment()
        this.playingFragment.arguments = bundle

        return this.myView
    }

    /**
     * Solicita permissão de GPS
     */
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

    /**
     * Obtém as playlists do Spotify do usuário logado
     */
    private fun getSpotifyPlaylists(screenPlaylists: List<PlayList>, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val request = Request.Builder()
                .url("https://api.spotify.com/v1/users/$userId/playlists")
                .addHeader("Authorization", "Bearer $userToken")
                .build()

        cancelCall()
        mCall = mOkHttpClient.newCall(request)

        mCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Request", "Failed to fetch data: $e")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body()?.string())

                    // Criar objetos do tipo PLAYLIST com os dados
                    Log.d("Response", jsonObject.toString())
                    var playlists: JSONArray = jsonObject.getJSONArray("items")

                    for (i in 0..(playlists.length() - 1)) {
                        val p = playlists.getJSONObject(i)

                        var displayName = p.getJSONObject("owner").getString("display_name").trim()
                        if (displayName == "null") {
                            displayName = "Desconhecido"
                        }

                        var playlist = PlayList(
                                p.getString("name"),
                                "por ${displayName}",
                                (p.getJSONArray("images").get(0) as JSONObject).getString("url"),
                                p.getString("id")
                        )
                        (screenPlaylists as ArrayList<PlayList>).add(playlist)
                    }

                    // A atualização da tela precisa ser feita na thread de UI
                    activity!!.runOnUiThread {
                        (adapter as PlayListsAdapter).notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.d("Request", "Failed to parse data: $e")
                }
            }
        })
    }

    private fun cancelCall() {
        mCall?.cancel()
    }

    /**
     * Implements necessários mas não usados
     */

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}