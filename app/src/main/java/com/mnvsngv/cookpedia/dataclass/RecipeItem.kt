package com.mnvsngv.cookpedia.dataclass

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

//Recipe Item dataclass
data class RecipeItem(
    val recipe_name: String = "",
    val recipe_image: String = "",
    val steps_list: ArrayList<StepsItem> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(null) as ArrayList<StepsItem>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(recipe_name)
        parcel.writeString(recipe_image)
        parcel.writeList(steps_list)
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