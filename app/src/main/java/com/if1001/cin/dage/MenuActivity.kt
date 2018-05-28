package com.if1001.cin.dage

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var homeFragment: HomeFragment
    private lateinit var pastWorkoutsFragment: PastWorkoutsFragment

    private val HOME_FRAGMENT_TAG = "homeTag"
    private val PAST_WORKOUTS_TAG = "pastWorkouts"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(app_toolbar)

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
                if (fragment == null){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                }else if (fragment.isHidden){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.homeFragment, HOME_FRAGMENT_TAG).commit()
                }
            }
            R.id.nav_logout -> {

            }
            R.id.nav_past -> {
                val fragment = fragmentManager.findFragmentByTag(PAST_WORKOUTS_TAG)
                if (fragment == null){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_TAG).commit()
                }else if (fragment.isHidden){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, this.pastWorkoutsFragment, PAST_WORKOUTS_TAG).commit()
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
