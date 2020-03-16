package uk.ac.aber.dcs.mmp.faa.datasources

import android.content.Context
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.User
import uk.ac.aber.dcs.mmp.faa.ui.main.MainActivity
import uk.ac.aber.dcs.mmp.faa.utils.SimpleObservableStringSet
import uk.ac.aber.dcs.mmp.faa.R

class DataService private constructor() {
    var user: FirebaseUser? = null
    private lateinit var database: FirebaseFirestore
    lateinit var mainActivity: MainActivity
    private var savedCats: SimpleObservableStringSet =
        SimpleObservableStringSet()
    private val FAVE_KEY = "FAVOURITE_KEY"

    companion object {
        val INSTANCE = DataService()
    }

    fun initialize(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
        database = Firebase.firestore
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