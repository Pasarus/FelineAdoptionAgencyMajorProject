package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.DocumentReference

@IgnoreExtraProperties
data class User(
    var addressLineOne: String? = "",
    var addressLineTwo: String? = "",
    var addressLineThree: String? = "",
    var county: String? = "",
    var name: String? = "",
    var mobileNumber: String? = "",
    var postCode: String? = "",
    var favouritedCats: List<String>? = ArrayList(),
    var adoptionProcesses: List<DocumentReference>? = ArrayList()
)