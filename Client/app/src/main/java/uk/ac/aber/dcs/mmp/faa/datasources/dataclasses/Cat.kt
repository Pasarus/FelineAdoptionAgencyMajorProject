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

class Cat : Parcelable {

    var catAgeMonths: Int? = 0
    var catBreed: String? = ""
    var catName: String? = ""
    var colour: String? = ""
    var description: String? = ""
    var disabled: Boolean? = false
    var location: String? = ""
    var neutered: Boolean? = false
    var pictureUrl: String? = ""
    var preferences: MutableMap<String, Boolean>? = HashMap()
    var sex: String? = ""
    var catId: String? = ""

    constructor() : super()  // Needed for Firebase
    constructor(
        catAgeMonths: Int?, catBreed: String?, catName: String?, color: String?,
        description: String?, disabled: Boolean?, location: String?, neutered: Boolean?,
        pictureId: String?, preferences: MutableMap<String, Boolean>?, sex: String?,
        catId: String?
    ) : this() {
        this.catAgeMonths = catAgeMonths
        this.catBreed = catBreed
        this.catName = catName
        this.colour = color
        this.description = description
        this.disabled = disabled
        this.location = location
        this.neutered = neutered
        this.pictureUrl = pictureId
        this.preferences = preferences
        this.sex = sex
        this.catId = catId
    }

    constructor(parcel: Parcel) : this() {
        catAgeMonths = parcel.readValue(Int::class.java.classLoader) as? Int
        catBreed = parcel.readString()
        catName = parcel.readString()
        colour = parcel.readString()
        description = parcel.readString()
        disabled = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        location = parcel.readString()
        neutered = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        pictureUrl = parcel.readString()
        sex = parcel.readString()
        catId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(catAgeMonths)
        parcel.writeString(catBreed)
        parcel.writeString(catName)
        parcel.writeString(colour)
        parcel.writeString(description)
        parcel.writeValue(disabled)
        parcel.writeString(location)
        parcel.writeValue(neutered)
        parcel.writeString(pictureUrl)
        parcel.writeString(sex)
        parcel.writeValue(catId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cat> {
        override fun createFromParcel(parcel: Parcel): Cat {
            return Cat(parcel)
        }

        override fun newArray(size: Int): Array<Cat?> {
            return arrayOfNulls(size)
        }
    }
}