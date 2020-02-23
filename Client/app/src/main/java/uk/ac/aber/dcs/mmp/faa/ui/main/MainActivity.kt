package uk.ac.aber.dcs.mmp.faa.ui.main

import android.os.Bundle
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.aber.dcs.mmp.faa.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val navController = nav_host_fragment.findNavController()

        NavigationUI.setupWithNavController(bottomNavigationBar, navController)
    }
}

