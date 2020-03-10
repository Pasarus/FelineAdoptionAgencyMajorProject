package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import android.os.Parcel
import android.os.Parcelable

class Cat : Parcelable {

    var catAgeMonths: Int? = 0
    var catBreed: String? = ""
    var catName: String? = ""
    var color: String? = ""
    var description: String? = ""
    var disabled: Boolean? = false
    var location: String? = ""
    var neutered: Boolean? = false
    var pictureUrl: String? = ""
    var preferences: MutableMap<String, Boolean>? = HashMap()
    var sex: String? = ""
    var catId: Int? = 0

    constructor() : super() {} // Needed for Firebase
    constructor(catAgeMonths: Int?, catBreed: String?, catName: String?, color: String?,
                description: String?,  disabled: Boolean?, location: String?, neutered: Boolean?,
                pictureId: String?, preferences: MutableMap<String, Boolean>?, sex: String?,
                catId: Int?) : this() {
        this.catAgeMonths = catAgeMonths
        this.catBreed = catBreed
        this.catName = catName
        this.color = color
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
        color = parcel.readString()
        description = parcel.readString()
        disabled = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        location = parcel.readString()
        neutered = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        pictureUrl = parcel.readString()
        sex = parcel.readString()
        catId = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(catAgeMonths)
        parcel.writeString(catBreed)
        parcel.writeString(catName)
        parcel.writeString(color)
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