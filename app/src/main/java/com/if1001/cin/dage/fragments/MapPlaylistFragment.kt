package com.if1001.cin.dage.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.if1001.cin.dage.*
import com.if1001.cin.dage.model.PlayList
import com.if1001.cin.dage.adapters.PlayListsAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_map_playlist.view.*
import kotlinx.android.synthetic.main.nav_header_menu.view.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MapPlaylistFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists: List<PlayList>
    private lateinit var userToken: String
    private lateinit var userId: String

    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // get token from bundle
        this.userToken = arguments!!.getString("userToken")
        this.userId = arguments!!.getString("userId")

        // get playlists from Spotify
        getSpotifyPlaylists()

        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_map_playlist, container, false)
        this.recyclerView = this.myView.recycle_view_playlist
        this.playlists = listOf(PlayList("list 1"), PlayList("list 2"), PlayList("list 3"))

        this.recyclerView.adapter = PlayListsAdapter(this.playlists,this.activity!!)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager

        return this.myView
    }

    fun getSpotifyPlaylists() {
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

                    // TODO criar objetos do tipo PLAYLIST com os dados
                    Log.d("Response", jsonObject.toString())
                } catch (e: JSONException) {
                    Log.d("Request", "Failed to parse data: $e")
                }
            }
        })
    }

    private fun cancelCall() {
        mCall?.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
