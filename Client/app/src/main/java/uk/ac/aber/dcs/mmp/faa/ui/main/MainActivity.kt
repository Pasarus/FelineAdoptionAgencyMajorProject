package uk.ac.aber.dcs.mmp.faa.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
    private val startDestinations = setOf(R.id.homeFragment, R.id.savedFragment, R.id.findCatFragment)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(bottomNavigationBar, navController)

        // Setup AppBar
        val appBarConfiguration = AppBarConfiguration(startDestinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navDrawer, navController)

        defineWhereBottomNavShows()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate actionBar menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun defineWhereBottomNavShows() {
        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            if (destination.id in startDestinations) {
                bottomNavigationBar.visibility = View.VISIBLE
            } else {
                bottomNavigationBar.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.actionSettingsButton -> {
                navController.navigate(R.id.settingsFragment)
                true
            } else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        NavigationUI.navigateUp(navController, drawerLayout)
        return super.onSupportNavigateUp()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.navDrawLogin -> {}
            R.id.navDraw
        }
    }
}

