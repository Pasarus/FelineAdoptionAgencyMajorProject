package uk.ac.aber.dcs.mmp.faa.datasources

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser

class DataService private constructor() {
    var user: FirebaseUser? = null

    lateinit var mainActivity: AppCompatActivity

    companion object {
        val INSTANCE = DataService()
    }
}