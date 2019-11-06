package com.ugdevs.smartbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?:return
        val navController = host.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.nav_search -> navController.navigate(R.id.dest_search)
                it.itemId == R.id.nav_more -> navController.navigate(R.id.dest_moreFragment)
                it.itemId == R.id.nav_wallet -> {

                }
            }
        true
        }
    }
}
