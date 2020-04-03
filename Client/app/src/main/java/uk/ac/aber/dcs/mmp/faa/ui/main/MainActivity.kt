package uk.ac.aber.dcs.mmp.faa.ui.main

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private val ADOPTION_STATUS_CHANGE_CHANNEL_ID = "ADOPTIONSTATUSCHANGECHANNELID"
    private val NEWLY_LISTED_CAT_CHANNEL_ID = "NEWLYLISTEDCATCHANNELID"
    private val ADOPTION_NEWS_CHANNEL_ID = "ADOPTIONNEWSCHANNELID"

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

        // Start the DataService
        DataService.INSTANCE.initialize(this)
        DataService.INSTANCE.loadPreferences()
        if (DataService.INSTANCE.darkMode){
            invalidateOptionsMenu()
        }

        // Handle already being logged in before navigation/starting the app for persistent login
        val user = DataService.INSTANCE.user
        if (user != null){
            updateNavDrawLoginTextAndImage(user)
        }

        // Create the notification channels
        // Adoption Status Change
        createNotificationChannel(getString(R.string.adoption_status_change),
            getString(R.string.adoption_status_change_description),
            ADOPTION_STATUS_CHANGE_CHANNEL_ID)

        // Newly listed cat
        createNotificationChannel(getString(R.string.newly_listed_cat),
            getString(R.string.newly_listed_cat_description),
            NEWLY_LISTED_CAT_CHANNEL_ID)

        // Adoption news
        createNotificationChannel(getString(R.string.adoption_news),
            getString(R.string.adoption_news_description),
            ADOPTION_NEWS_CHANNEL_ID)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (DataService.INSTANCE.darkMode){
            val settingsItem = menu.findItem(R.id.actionSettingsButton)
            settingsItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_settings_white_24dp)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onStop() {
        super.onStop()
        DataService.INSTANCE.syncSavedCats()
        DataService.INSTANCE.savePreferences()
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

    fun doLogin() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
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

        DataService.INSTANCE.savedCats.updateObserversAddBlank()
    }

    private fun updateNavDrawLoginTextAndImage(user: FirebaseUser?){
        if (user != null) {
            navDrawLoginText.text = "My Account"
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(navDrawLoginImage)
            } else {
                navDrawLoginImage.setImageResource(R.drawable.ic_face_orange_24dp)
            }
        }
    }

    private fun createNotificationChannel(channelName: String, channelDescription: String, channelID: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, channelName, importance).apply {
                description = channelDescription
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

