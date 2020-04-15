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

package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.utils.boolToString
import uk.ac.aber.dcs.mmp.faa.utils.stringToBool

/**
 * This is a data class that is based entirely on the formation of data in the google firebase
 * firestore. It is equal to that of the firestore data files inside of the collection named
 * "adoptionProcesses" it allows for useful transport of data between fragments, and the backend
 * database.
 */
class AdoptionProcess : Parcelable {
    var cat: DocumentReference? = null
    var status: MutableMap<String, Any>? = HashMap()
    var user: DocumentReference? = null

    constructor() : super()  // Needed for Firebase
    constructor(status: Map<String, Any>, cat: Cat) : this() {
        this.status = status.toMutableMap()
        this.cat = FirebaseFirestore.getInstance().collection("cats")
            .document("cat" + cat.catId)

        this.user = FirebaseFirestore.getInstance().collection("users").document(
            DataService.INSTANCE.user!!.uid
        )
    }

    constructor(parcel: Parcel) : this() {
        status!!["pending"] = stringToBool(parcel.readString() as String)
        status!!["pendingReason"] = parcel.readString() as String
        status!!["accepted"] = stringToBool(parcel.readString() as String)
        status!!["rejected"] = stringToBool(parcel.readString() as String)
        status!!["rejectedReason"] = parcel.readString() as String
        user = FirebaseFirestore.getInstance().document(parcel.readString() as String)
        cat = FirebaseFirestore.getInstance().document(parcel.readString() as String)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null){
            val pending = status!!["pending"] as Boolean
            val pendingReason = status!!["pendingReason"] as String
            val accepted = status!!["accepted"] as Boolean
            val rejected = status!!["rejected"] as Boolean
            val rejectedReason = status!!["rejectedReason"] as String
            val user = this.user.toString()
            val cat = this.cat.toString()
            dest.writeString(boolToString(pending))
            dest.writeString(pendingReason)
            dest.writeString(boolToString(accepted))
            dest.writeString(boolToString(rejected))
            dest.writeString(rejectedReason)
            dest.writeString(user)
            dest.writeString(cat)
        }
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