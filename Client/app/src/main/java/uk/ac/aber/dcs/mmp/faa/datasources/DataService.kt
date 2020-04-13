/*   Copyright 2020 Samuel Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * The DataService is a universal class that is used throughout the application. The intention is
 * to remove the need for spaghetti code that can be found in some applications. If it is to do with
 * getting application wide data, you get it from this class. This class has a companion object,
 * this object is aimed at allowing one specific object be accessible in each of the accesses of any
 * data held by this object. I.e. anywhere in the app, one object holds a significant portion of data
 * This simplifies structure immensely and allows for further maintenance and development.
 */
class DataService private constructor() {
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        set(user) {
            field = user
            updateSavedCatsFromUser()
        }

    private fun updateSavedCatsFromUser() {
        if (user != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(user!!.uid).get().addOnSuccessListener { document ->
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
    private var onStopCalledCount = 0
    fun shouldMainActivitySyncOnStop(): Boolean {
        // When darkmode is applied, onStop is called before we can
        // receive the current favourite cat's state from the Firestore, this means we must allow
        // for 1 extra call to ensure the darkmode state can be set correctly
        val returnValue = if (darkMode && onStopCalledCount >= 1){
            true
        } else !darkMode
        onStopCalledCount += 1
        return returnValue
    }

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

        if (settings.getOrDefault(DARK_MODE_KEY, false)) {
            darkMode()
        } else {
            lightMode()
        }

        // Load back the current saved cat state
        updateSavedCatsFromUser()
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
        val addressLineOne =
            view.findViewById<TextInputEditText>(R.id.addressLineOneTextField).text.toString()
        val addressLineTwo =
            view.findViewById<TextInputEditText>(R.id.addressLineTwoTextField).text.toString()
        val addressLineThree =
            view.findViewById<TextInputEditText>(R.id.addressLineThreeTextField).text.toString()
        val postCode = view.findViewById<TextInputEditText>(R.id.postCodeTextField).text.toString()
        val county = view.findViewById<TextInputEditText>(R.id.countyTextField).text.toString()
        val phone = view.findViewById<TextInputEditText>(R.id.phoneNumberTextField).text.toString()


        val newUser = User(
            addressLineOne,
            addressLineTwo,
            addressLineThree,
            county,
            name,
            phone,
            postCode,
            favouritedCats = savedCats.toList()
        )
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

        database.collection("users").document(user!!.uid).get().addOnSuccessListener { document ->
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