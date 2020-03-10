package uk.ac.aber.dcs.mmp.faa.datasources

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.CatList
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.User
import uk.ac.aber.dcs.mmp.faa.utils.SimpleObservableStringSet

class DataService private constructor() {
    var user: FirebaseUser? = null
    private lateinit var database: DatabaseReference
    lateinit var mainActivity: AppCompatActivity
    private var savedCats: SimpleObservableStringSet =
        SimpleObservableStringSet()
    private val FAVE_KEY = "FAVOURITE_KEY"

    companion object {
        val INSTANCE = DataService()
    }

    fun initialize(mainActivity: AppCompatActivity) {
        this.mainActivity = mainActivity
        database = FirebaseDatabase.getInstance().reference
    }

    fun loadSavedCats() {
        val preferences = mainActivity.getPreferences(Context.MODE_PRIVATE)
        savedCats = SimpleObservableStringSet(
            preferences.getStringSet(
                FAVE_KEY,
                HashSet()
            )!!
        )
    }

    fun saveSavedCats() {
        val preferences = mainActivity.getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putStringSet(FAVE_KEY, savedCats)
        editor.apply()
    }

    fun addNewUserToDatabase(userId: String, addressLineOne: String, addressLineTwo: String,
                             addressLineThree: String, county: String, name: String,
                             mobileNumber: String, postCode: String) {
        val user = User(addressLineOne, addressLineTwo, addressLineThree, county, name,
            mobileNumber, postCode)
        database.child("users").child(userId).setValue(user)
    }

    fun retrieveCats() {
        val catListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(CatList::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DataService", "loadPost:onCancelled", databaseError.toException())
            }
        }
        val cats = database.child("cats").addListenerForSingleValueEvent(catListener)
    }

    fun isCatFavourite(catId: Int?): Boolean {
        return "$catId" in savedCats
    }
}