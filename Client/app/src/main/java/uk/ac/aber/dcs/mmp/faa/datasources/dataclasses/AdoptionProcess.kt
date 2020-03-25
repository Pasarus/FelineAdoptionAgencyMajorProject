package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

class AdoptionProcess : Parcelable {
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

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdoptionProcess> {
        override fun createFromParcel(parcel: Parcel): AdoptionProcess {
            return AdoptionProcess(parcel)
        }

        override fun newArray(size: Int): Array<AdoptionProcess?> {
            return arrayOfNulls(size)
        }
    }

}