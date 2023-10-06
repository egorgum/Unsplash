package com.example.unsplash.ui.activities

import android.os.Bundle
import android.view.View
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unsplash.R
import com.example.unsplash.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_collections, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)



        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destination,_ ->
            if (destination.id == R.id.onboardFragment || destination.id == R.id.authorizeFragment){
                binding.navView.visibility = View.GONE
            }
            else{
                binding.navView.visibility = View.VISIBLE
            }
        }
    }
}