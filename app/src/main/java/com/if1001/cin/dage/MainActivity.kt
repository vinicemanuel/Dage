package com.if1001.cin.dage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.eqot.fontawesome.FontAwesome
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1337
    private lateinit var CLIENT_ID: String
    private lateinit var REDIRECT_URI: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FontAwesome.applyToAllViews(this, findViewById(R.id.button_login_spotify))

        ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.INTERNET), 1)

        // Get info from strings
        CLIENT_ID = getString(R.string.spotify_client_id)
        REDIRECT_URI = "${getString(R.string.com_spotify_sdk_redirect_scheme)}://${getString(R.string.com_spotify_sdk_redirect_host)}"

        Log.d("ClientId", CLIENT_ID)
        Log.d("Redirect", REDIRECT_URI)
        var builder: AuthenticationRequest.Builder =
                AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-read-private", "user-read-email", "playlist-read-private", "streaming"))
        var request: AuthenticationRequest = builder.build()

        // Clique no 'Login com Spotify'
        button_login_spotify.setOnClickListener(View.OnClickListener {
            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
        })

        val user = AppDatabase.getInstance(applicationContext).UserDao().findUSer()
        if (user != null) {
            Log.d("user_saved", "${user.name} ${user.email}")
            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            var response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
            // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    Log.d("Token", response.accessToken)
                    var i = Intent(applicationContext, MenuActivity::class.java)
                    i.putExtra("ACCESS_TOKEN", response.accessToken)
                    applicationContext.startActivity(i)
                }

            // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                    Log.d("Error", response.error)
                }
            }// Handle successful response
            // Handle error response
            // Most likely auth flow was cancelled
            // Handle other cases
        }
    }
}