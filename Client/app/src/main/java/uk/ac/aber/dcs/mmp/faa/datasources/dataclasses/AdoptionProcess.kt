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

class AdoptionProcess : Parcelable {
    var cat: DocumentReference? = null
    var status: Map<String, Any>? = HashMap()
    var user: DocumentReference? = null

    constructor() : super()  // Needed for Firebase
    constructor(status: Map<String, Any>, cat: Cat) : this() {
        this.status = status
        this.cat = FirebaseFirestore.getInstance().collection("cats")
            .document("cat" + cat.catId)

        this.user = FirebaseFirestore.getInstance().collection("users").document(
            DataService.INSTANCE.user!!.uid
        )
    }

    constructor(parcel: Parcel) : this() {
        TODO("Not yet implemented")
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