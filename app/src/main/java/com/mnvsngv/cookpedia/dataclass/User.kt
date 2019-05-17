package com.mnvsngv.cookpedia.dataclass

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class User(
    val email: String = "",
    val username: String = "",
    val name: String = "",
    val user_recipes: ArrayList<RecipeItem> = arrayListOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(null) as ArrayList<RecipeItem>
    )

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(email)
        parcel?.writeString(username)
        parcel?.writeString(name)
        parcel?.writeList(user_recipes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
