package com.if1001.cin.dage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.fragments.HomeFragment
import com.if1001.cin.dage.fragments.PastWorkoutsFragment
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var homeFragment: HomeFragment
    private lateinit var pastWorkoutsFragment: PastWorkoutsFragment
    private lateinit var CLIENT_ID: String
    private lateinit var userToken: String

    val AUTH_TOKEN_REQUEST_CODE = 0x10

    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null

    private val HOME_FRAGMENT_TAG = "homeTag"
    private val PAST_WORKOUTS_TAG = "pastWorkouts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(app_toolbar)

        // Get info from strings
        CLIENT_ID = getString(R.string.spotify_client_id)

        userToken = intent.getStringExtra("ACCESS_TOKEN")
        getUserProfile()
        //var playerConfig = Config(this, userToken, CLIENT_ID)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, app_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        this.homeFragment = HomeFragment()
        this.pastWorkoutsFragment = PastWorkoutsFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val fragment = fragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                if (fragment == null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                } else if (fragment.isHidden) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                }
            }
            R.id.nav_logout -> {

            }
            R.id.nav_past -> {
                val fragment = fragmentManager.findFragmentByTag(PAST_WORKOUTS_TAG)
                if (fragment == null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_TAG).commit()
                } else if (fragment.isHidden) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_TAG).commit()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        cancelCall()
        super.onDestroy()
    }

    fun getUserProfile() {
        val request = Request.Builder()
                .url("https://api.spotify.com/v1/me")
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
                    Log.d("Response", jsonObject.toString())

                    // UI changes have to be done into UI Thread
                    runOnUiThread {
                        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                        val hView = navigationView.inflateHeaderView(R.layout.nav_header_menu)

                        val imageView = hView.findViewById(R.id.imageView) as ImageView
                        val tv = hView.findViewById(R.id.user_name) as TextView

                        Picasso.get().load(jsonObject.getJSONArray("images").getJSONObject(0).getString("url")).into(imageView)
                        tv.setText(jsonObject.getString("display_name"))
                    }
                } catch (e: JSONException) {
                    Log.d("Request", "Failed to parse data: $e")
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE === requestCode) {
            userToken = response.accessToken
        }
    }

    private fun cancelCall() {
        mCall?.cancel()
    }

    private fun getRedirectUri(): Uri {
        return Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build()
    }
}
