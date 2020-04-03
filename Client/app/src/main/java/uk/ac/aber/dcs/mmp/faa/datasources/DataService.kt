package uk.ac.aber.dcs.mmp.faa.datasources

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.User
import uk.ac.aber.dcs.mmp.faa.ui.main.MainActivity
import uk.ac.aber.dcs.mmp.faa.utils.SimpleObservableStringSet


class DataService private constructor() {
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    set(user) {
        field = user
        if (user != null){
            FirebaseFirestore.getInstance().collection("users")
                .document(user.uid).get().addOnSuccessListener {
                document ->
                    val userObject: User? = document.toObject()
                    if (userObject != null) {
                        // User has an object so update the saved cats list
                        savedCats.clear()
                        savedCats.addAll(userObject.favouritedCats!!)
                    }
            }
        }
    }
    private lateinit var database: FirebaseFirestore
    lateinit var mainActivity: MainActivity
    var savedCats: SimpleObservableStringSet =
        SimpleObservableStringSet()

    val DARK_MODE_KEY = "darkMode"
    var settings = mutableMapOf(
        DARK_MODE_KEY to false
    )
    var darkMode = false

    companion object {
        val INSTANCE = DataService()
    }

    fun initialize(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
        database = Firebase.firestore
    }

    fun loadPreferences() {
        val preferences: SharedPreferences = mainActivity.getPreferences(Context.MODE_PRIVATE)
        settings = mutableMapOf(DARK_MODE_KEY to preferences.getBoolean(DARK_MODE_KEY, false))

        if (settings.getOrDefault(DARK_MODE_KEY, false)){
            darkMode()
        } else {
            lightMode()
        }
    }

    fun darkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        darkMode = true
    }

    fun lightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        darkMode = false
    }

    fun savePreferences() {
        val preferences: SharedPreferences = mainActivity.getPreferences(Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(DARK_MODE_KEY, settings.getOrElse(DARK_MODE_KEY) { false })
        editor.apply()
    }

    fun syncSavedCats() {
        if (user != null) {
            FirebaseFirestore.getInstance().collection("users").document(user!!.uid)
                .update(mapOf("favouritedCats" to savedCats.toList()))
        }
    }

    fun isCatFavourite(catId: String?): Boolean {
        return "$catId" in savedCats
    }

    /**
     * This assumes that you have using the user_data.xml layout in the view parameter.
     */
    fun updateUserDataForView(view: View) {
        val name = view.findViewById<TextInputEditText>(R.id.nameTextField).text.toString()
        val addressLineOne = view.findViewById<TextInputEditText>(R.id.addressLineOneTextField).text.toString()
        val addressLineTwo = view.findViewById<TextInputEditText>(R.id.addressLineTwoTextField).text.toString()
        val addressLineThree = view.findViewById<TextInputEditText>(R.id.addressLineThreeTextField).text.toString()
        val postCode = view.findViewById<TextInputEditText>(R.id.postCodeTextField).text.toString()
        val county = view.findViewById<TextInputEditText>(R.id.countyTextField).text.toString()
        val phone = view.findViewById<TextInputEditText>(R.id.phoneNumberTextField).text.toString()


        val newUser = User(addressLineOne, addressLineTwo, addressLineThree, county, name, phone, postCode, favouritedCats = savedCats.toList())
        val userId = user!!.uid
        database.collection("users").document(userId).set(newUser)
    }

    /**
     * This assumes that you have using the user_data.xml layout in the view parameter.
     */
    fun getDataForViewFromFirestore(view: View) {
        val name = view.findViewById<TextInputEditText>(R.id.nameTextField)
        val addressLineOne = view.findViewById<TextInputEditText>(R.id.addressLineOneTextField)
        val addressLineTwo = view.findViewById<TextInputEditText>(R.id.addressLineTwoTextField)
        val addressLineThree = view.findViewById<TextInputEditText>(R.id.addressLineThreeTextField)
        val postCode = view.findViewById<TextInputEditText>(R.id.postCodeTextField)
        val county = view.findViewById<TextInputEditText>(R.id.countyTextField)
        val phone = view.findViewById<TextInputEditText>(R.id.phoneNumberTextField)

        database.collection("users").document(user!!.uid).get().addOnSuccessListener {
                document ->
            if (document != null) {
                name.setText(document.get("name").toString())
                addressLineOne.setText(document.get("addressLineOne").toString())
                addressLineTwo.setText(document.get("addressLineTwo").toString())
                addressLineThree.setText(document.get("addressLineThree").toString())
                postCode.setText(document.get("postCode").toString())
                county.setText(document.get("county").toString())
                phone.setText(document.get("mobileNumber").toString())
            } else {
                name.setText(user!!.displayName)
                phone.setText(user!!.phoneNumber)
            }
        }
    }
}