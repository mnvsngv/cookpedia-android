package com.mnvsngv.cookpedia.dataclass

import java.io.Serializable

data class RecipeIngredient(var name: String = "", var quantity: String = "", var serving: String = "") : Serializable