package com.if1001.cin.dage

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.if1001.cin.dage.fragments.HomeFragment
import com.if1001.cin.dage.fragments.PastWorkoutsFragment
import com.if1001.cin.dage.model.User
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.nav_header_menu.view.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Activity principal, onde o app em si roda, ela monta os fragments e os chama
 */
class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var homeFragment: HomeFragment
    private lateinit var pastWorkoutsFragment: PastWorkoutsFragment

    // Dados relativos ao Spotify
    private lateinit var CLIENT_ID: String
    private lateinit var REDIRECT_URI: String
    private lateinit var userToken: String

    val AUTH_TOKEN_REQUEST_CODE = 0x10

    private val mOkHttpClient = OkHttpClient()
    private var mCall: Call? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(app_toolbar)

        // Get info from strings
        CLIENT_ID = getString(R.string.spotify_client_id)
        REDIRECT_URI = "${getString(R.string.com_spotify_sdk_redirect_scheme)}://${getString(R.string.com_spotify_sdk_redirect_host)}"
        userToken = intent.getStringExtra("ACCESS_TOKEN")

        // mount user profile
        getUserProfile()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, app_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Navegação do menu lateral
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
        // Home (mapa e play para selecionar playlist)
            R.id.nav_home -> {
                val fragment = fragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                if (fragment == null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                } else if (fragment.isHidden) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                }
            }
        // Trocar account vinculado
            R.id.nav_logout -> {
                val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                        .setShowDialog(true)
                        .setScopes(arrayOf("user-read-private", "user-read-email", "playlist-read-private", "streaming"))

                var request: AuthenticationRequest = builder.build()
                AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
            }
        // Workouts
            R.id.nav_past -> {
                val fragment = fragmentManager.findFragmentByTag(PAST_WORKOUTS_FRAGMENT_TAG)
                if (fragment == null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_FRAGMENT_TAG).commit()
                } else if (fragment.isHidden) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_FRAGMENT_TAG).commit()
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

    /**
     * Obtém dados do perfil do Spotify do usuário
     */
    private fun getUserProfile() {
        // Montar chamada de API
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

            /**
             * Resposta da chamada de API
             */
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                try {
                    val jsonObject = JSONObject(response.body()?.string())
                    Log.d("Response", jsonObject.toString())

                    // Obter dados da resposta
                    val displayName = jsonObject.getString(SPOTIFY_JSON_KEY_DISPLAY_NAME)
                    val imageURL = jsonObject.getJSONArray(SPOTIFY_JSON_KEY_IMAGES).getJSONObject(0).getString(SPOTIFY_JSON_KEY_IMAGE_URL)
                    val email = jsonObject.getString(SPOTIFY_JSON_KEY_EMAIL)
                    val userId = jsonObject.getString(SPOTIFY_JSON_KEY_ID)

                    // Criar objeto da classe e salvar na base de dados local
                    val user = User(imageURL, displayName, email, userId)
                    AppDatabase.getInstance(applicationContext).UserDao().insertUser(user)

                    Log.d("USER_INFOS", "$displayName $imageURL $email $userId")

                    // Passar dados necessários para o fragment
                    val bundle = Bundle()
                    bundle.putString("userToken", userToken)
                    bundle.putString("userId", userId)

                    homeFragment = HomeFragment()
                    homeFragment.arguments = bundle

                    pastWorkoutsFragment = PastWorkoutsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, homeFragment, HOME_FRAGMENT_TAG).commit()

                    // Atualizar UI com dados obtidos
                    runOnUiThread {
                        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                        val hView = navigationView.inflateHeaderView(R.layout.nav_header_menu)

                        val imageView = hView.imageView
                        val tv = hView.user_name

                        // Caso a imagem venha de perfil de Fcebook, obter url da imagem da API deles
                        if (imageURL.contains("facebook")) {
                            var fbUrl = "https://graph.facebook.com/${imageURL.split("&").get(0).split("=").get(1)}/picture?width=64&height=64"
                            Picasso.get().load(fbUrl).into(imageView)
                        } else {
                            Picasso.get().load(imageURL).into(imageView)
                        }

                        tv.text = displayName
                    }
                } catch (e: JSONException) {
                    Log.d("Request", "Failed to parse data: $e")
                }
            }
        })
    }

    /**
     * Resultado da chamada de API de autorização (obtenção de Token)
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            userToken = response.accessToken
        }
    }

    private fun cancelCall() {
        mCall?.cancel()
    }
}
