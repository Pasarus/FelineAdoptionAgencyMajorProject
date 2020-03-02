package uk.ac.aber.dcs.mmp.faa.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val startDestinations = setOf(R.id.homeFragment, R.id.savedFragment, R.id.findCatFragment)
    private val RC_SIGN_IN = 0

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
                if (DataService.INSTANCE.user != null) {
                    navController.navigate(R.id.myAccountFragment)
                } else {
                    doLogin()
                }
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
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                DataService.INSTANCE.user = user
                updateNavDrawLoginTextAndImage(user)
            }
        }
    }

    private fun updateNavDrawLoginTextAndImage(user: FirebaseUser?){
        if (user != null) {
            navDrawLogin.text = "My Account"
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(navDrawLoginImage)
            } else {
                navDrawLoginImage.setImageResource(R.drawable.ic_face_black_24dp)
            }
        }
    }
}

