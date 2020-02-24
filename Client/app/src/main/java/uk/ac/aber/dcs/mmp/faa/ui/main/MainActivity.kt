package uk.ac.aber.dcs.mmp.faa.ui.main

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.aber.dcs.mmp.faa.R
import java.lang.reflect.Array.set

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(bottomNavigationBar, navController)

        // Setup AppBar
        val startDestinations = setOf(R.id.homeFragment, R.id.savedFragment, R.id.findCatFragment)
        val appBarConfiguration = AppBarConfiguration(startDestinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navDrawer, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate actionBar menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.actionSettingsButton -> {
                navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        NavigationUI.navigateUp(navController, drawerLayout)
        return super.onSupportNavigateUp()
    }
}

