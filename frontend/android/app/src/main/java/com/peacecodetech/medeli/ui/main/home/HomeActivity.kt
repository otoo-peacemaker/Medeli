package com.peacecodetech.medeli.ui.main.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        val navView: BottomNavigationView = binding.navView

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_pharmacy,
                R.id.navigation_search,
                R.id.navigation_chat,
                R.id.navigation_profile
            )
        )//Not providing back navigation to bottom nav
        navView.setupWithNavController(navController.navController)//set up the bottom navigation
        setupActionBarWithNavController(navController.navController, appBarConfiguration) //navigation with support action bar

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /** Handle action bar item clicks here. The action bar will
     *automatically handle clicks on the Home/Up button, so long
     *as you specify a parent activity in AndroidManifest.xml.
     * Now, when a user clicks the page_fragment menu item, the app automatically navigates to the corresponding destination with the same id.
     * */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

//https://developer.android.com/guide/navigation/navigation-ui