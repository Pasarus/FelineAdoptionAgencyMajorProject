package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

class AdoptionProcess {
    var cat: DocumentReference? = null
    var status: Map<String, Any>? = HashMap()
    var user: DocumentReference? = null

    constructor() : super()  // Needed for Firebase
    constructor(status: Map<String, Any>, cat: Cat) : this() {
        this.status = status
        this.cat = FirebaseFirestore.getInstance().collection("cats")
            .document("cat"+cat.catId)

        this.user = FirebaseFirestore.getInstance().collection("users").document(
            DataService.INSTANCE.user!!.uid)
    }

}