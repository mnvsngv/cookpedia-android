package com.mnvsngv.cookpedia.dataclass

import android.os.Parcel
import android.os.Parcelable
import java.util.*

//Recipe Item dataclass
data class RecipeItem(
    val name: String = "",
    val image: String = "",
    val steps: ArrayList<RecipeStep> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(null) as ArrayList<RecipeStep>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeList(steps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeItem> {
        override fun createFromParcel(parcel: Parcel): RecipeItem {
            return RecipeItem(parcel)
        }

        override fun newArray(size: Int): Array<RecipeItem?> {
            return arrayOfNulls(size)
        }
    }
}