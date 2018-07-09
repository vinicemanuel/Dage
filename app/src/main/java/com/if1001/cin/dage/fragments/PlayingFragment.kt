package com.if1001.cin.dage.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
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
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.if1001.cin.dage.*
import com.if1001.cin.dage.R
import com.if1001.cin.dage.adapters.ContentListenerMusic
import com.if1001.cin.dage.adapters.MusicAdapter
import com.if1001.cin.dage.model.Music
import com.if1001.cin.dage.model.PlayList
import com.if1001.cin.dage.model.Workout
import com.spotify.sdk.android.player.*
import kotlinx.android.synthetic.main.fragment_playing.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Fragment responsável pelo mapa e playlist durante workout
 */
class PlayingFragment : Fragment(), OnMapReadyCallback, LocationListener, Player.NotificationCallback, ConnectionStateCallback, ContentListenerMusic {
    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var mMap: GoogleMap? = null
    private lateinit var mLocationManager: LocationManager
    private lateinit var mGeocoder: Geocoder
    private lateinit var mapView: MapView
    private lateinit var route: MutableList<PointF>
    private var enableTracking = false
    private lateinit var routeLine: PolylineOptions
    private var locationName: String = "mei da rua"
    lateinit var playListPlaingName: String
    lateinit var playList: PlayList
    lateinit var musics: List<Music>
    lateinit var music: Music
    private lateinit var userToken: String
    private lateinit var userId: String

    private var mPlayer: SpotifyPlayer? = null
    private lateinit var CLIENT_ID: String
    private var lastPosition = 0

    /**
     * Inicialização do mapa
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        try {
            this.mMap?.isMyLocationEnabled = true
//            this.mMap?.uiSettings?.isZoomGesturesEnabled = false
        } catch (e: SecurityException) {
            Log.d("Permission: ", "negando permissão")
        }
    }

    /**
     * Ocorre ao tocar em cada música da playlist exibida na tela
     */
    override fun onItemClicked(item: Music) {
        Log.d("click", "${item.MusicName}")

        val fragment = fragmentManager!!.findFragmentByTag(PLAYING_SONG_FRAGMENT_TAG)
        this.playListPlaingName = item.MusicName
        this.music = item

        if (fragment == null) {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this, PLAYING_SONG_FRAGMENT_TAG).commit()
        } else if (fragment.isHidden) {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this, PLAYING_SONG_FRAGMENT_TAG).commit()
        }

        var index = musics.indexOfFirst { m -> m.id == this.music.id }
        mPlayer!!.playUri(null, "spotify:playlist:${playList.id}", index, 0)
        lastPosition = index
        this.myView.playlist_playing.text = "Now playing: ${this.musics.get(lastPosition).MusicName}"
    }

    /**
     * Quando a localização mudar
     */
    override fun onLocationChanged(location: Location) {
        val myPlace = LatLng(location.latitude, location.longitude)

        this.mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 18.0f))

        Log.d("location: ", "update location " + location.toString())

        if (enableTracking) {
            this.route.add(PointF(location.latitude.toFloat(), location.longitude.toFloat()))
            Log.d("save_pint", "${this.route}")
            this.routeLine.add(myPlace).width(5.0f).color(Color.BLACK)
            this.mMap?.addPolyline(this.routeLine)
        }

        val addresses = this.mGeocoder.getFromLocation(location.latitude, location.longitude, 1)

        val city = addresses[0].locality
        val address = addresses[0].getAddressLine(0)
        val fullAddress = "$city, $address"
        this.locationName = fullAddress
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Mata o player ao sair da tela
     */
    override fun onDestroy() {
        Spotify.destroyPlayer(this)
        super.onDestroy()
    }

    /**
     * Setup da criação da view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        CLIENT_ID = getString(R.string.spotify_client_id)

        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_playing, container, false)
        this.recyclerView = this.myView.recycle_music_list

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager

        this.mapView = this.myView.mapView_music_list

        this.requestUserPermissions()

        this.route = mutableListOf()

        this.routeLine = PolylineOptions()

        this.userToken = arguments!!.getString("userToken")
        this.userId = arguments!!.getString("userId")

        val playerConfig = Config(this.context, userToken, CLIENT_ID)
        Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
            override fun onInitialized(spotifyPlayer: SpotifyPlayer) {
                mPlayer = spotifyPlayer
                (mPlayer as SpotifyPlayer).addConnectionStateCallback(this@PlayingFragment)
                (mPlayer as SpotifyPlayer).addNotificationCallback(this@PlayingFragment)
            }

            override fun onError(throwable: Throwable) {
                Log.e("MainActivity", "Could not initialize player: " + throwable.message)
            }
        })

        // get musics from Spotify
        this.musics = ArrayList()
        this.recyclerView.adapter = MusicAdapter(this.musics, this.activity!!, this)
        getSpotifyPlaylistSongs(this.musics, this.recyclerView.adapter)

        // Botão 'play'
        this.myView.play_button.setOnClickListener {
            Log.d("play", "play clicked")

            // não está trakeando (dar play)
            if (!enableTracking) {
                enableTracking = true
                this.myView.play_button.setImageResource(android.R.drawable.ic_media_pause)
                mPlayer!!.resume(null)
            } else {
                // está traqueando (dar pause)
                enableTracking = false
                this.myView.play_button.setImageResource(android.R.drawable.ic_media_play)
                mPlayer!!.pause(null)

                this.saveInstance()
            }
        }

        // Botão 'next'
        this.myView.ff_btn.setOnClickListener {
            mPlayer!!.skipToNext(null)
            this.myView.playlist_playing.text = "Now playing: ${this.musics.get(lastPosition).MusicName}"
        }

        // Botão 'previous'
        this.myView.back_btn.setOnClickListener {
            mPlayer!!.skipToPrevious(null)
            this.myView.playlist_playing.text = "Now playing: ${this.musics.get(lastPosition).MusicName}"
        }

        // Botão 'save'
        this.myView.end_btn.setOnClickListener {
            mPlayer!!.shutdown()
            enableTracking = false
            this.saveInstance()

            this.myView.play_button.setImageResource(android.R.drawable.ic_media_play)
            Toast.makeText(context, "Workout saved!", Toast.LENGTH_LONG)
        }

        this.myView.playlist_playing.text = "Now playing: ${this.playListPlaingName}"

        return this.myView
    }

    private fun saveInstance() {
        var workout = Workout(this.locationName, ListPointConverter().routeListToGson(this.route), playListPlaingName)
        AppDatabase.getInstance(context!!).WorkoutDao().insertWorkout(workout)
    }

    /**
     * Requer permissões de GPS
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

    /**
     * Configura o mapa
     */
    private fun configMap() {
        this.mapView.onCreate(null)
        this.mapView.onResume()
        this.mapView.getMapAsync(this)
    }

    /**
     * Obtém as músicas da playlist selecionada
     */
    private fun getSpotifyPlaylistSongs(screenMusics: List<Music>, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val request = Request.Builder()
                .url("https://api.spotify.com/v1/users/$userId/playlists/${playList.id}/tracks")
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

                    // TODO criar objetos do tipo MUSIC com os dados
                    Log.d("Response", jsonObject.toString())
                    var songs: JSONArray = jsonObject.getJSONArray("items")

                    for (i in 0..(songs.length() - 1)) {
                        val song = songs.getJSONObject(i).getJSONObject("track")
                        Log.d("Response", song.toString())

                        var music = Music(
                                song.getString("name"),
                                (song.getJSONArray("artists").get(0) as JSONObject).getString("name"),
                                song.getJSONObject("album").getString("name"),
                                (song.getJSONObject("album").getJSONArray("images").get(0) as JSONObject).getString("url"),
                                song.getString("id")
                        )
                        (screenMusics as ArrayList<Music>).add(music)
                    }

                    // A atualização da tela precisa ser feita na thread de UI
                    activity!!.runOnUiThread {
                        (adapter as MusicAdapter).notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.d("Request", "Failed to parse data: $e")
                }
            }
        })
    }

    /**
     * Cancela a chamada à API do Spotify
     */
    private fun cancelCall() {
        mCall?.cancel()
    }


    /**
     *
     * Controles relativos ao Spotify abaixo
     *
     */


    override fun onPlaybackEvent(playerEvent: PlayerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name)
        when (playerEvent.name) {
        // Handle event type as necessary
            "kSpPlaybackNotifyTrackChanged" -> {
                this.music = musics.get(lastPosition)
                this.myView.playlist_playing.text = "Now playing: ${this.musics.get(lastPosition).MusicName}"
                lastPosition += 1
            }
            "kSpPlaybackNotifyPrev" -> {
                if (lastPosition >= 2) {
                    lastPosition -= 2
                } else if (lastPosition >= 1) {
                    lastPosition -= 1
                }
            }
            else -> {
            }
        }
    }

    override fun onPlaybackError(error: Error) {
        Log.d("MainActivity", "Playback error received: " + error.name)
        when (error) {
        // Handle error type as necessary
            else -> {
            }
        }
    }

    override fun onLoggedIn() {
        Log.d("MainActivity", "User logged in")

        // Start playlist
        mPlayer!!.playUri(null, "spotify:playlist:${playList.id}", 0, 0)
        lastPosition = 0

        this.myView.play_button.setImageResource(android.R.drawable.ic_media_pause)

        // track location
        enableTracking = true
    }

    override fun onLoggedOut() {
        Log.d("MainActivity", "User logged out")
    }

    override fun onLoginFailed(var1: Error) {
        Log.d("MainActivity", "Login failed")
    }

    override fun onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred")
    }

    override fun onConnectionMessage(message: String) {
        Log.d("MainActivity", "Received connection message: $message")
    }
}
