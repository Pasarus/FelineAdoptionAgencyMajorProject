package uk.ac.aber.dcs.mmp.faa.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.aber.dcs.mmp.faa.R

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
        NavigationUI.setupWithNavController(navDrawerNavView, navController)

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
        if (navController.currentDestination?.id in startDestinations){
            drawerLayout.openDrawer(navDrawerNavView)
        } else {
            NavigationUI.navigateUp(navController, drawerLayout)
        }
        return super.onSupportNavigateUp()
    }

    fun navDrawOptionOnClick(view: View) {
        when (view.id) {
            R.id.navDrawSettings -> {
                navController.navigate(R.id.settingsFragment)
            }
            R.id.navDrawLogin -> {
                // Do login
                doLogin()
            }
            R.id.navDrawFeedback -> {
                navController.navigate(R.id.feedbackFragment)
            }
            R.id.navDrawAbout -> {
                navController.navigate(R.id.aboutFragment)
            }
            R.id.navDrawHelp -> {
                navController.navigate(R.id.helpFragment)
            }
        }

        // Close NavDraw
        drawerLayout.closeDrawers()
    }

    private fun doLogin() {

    }
}

