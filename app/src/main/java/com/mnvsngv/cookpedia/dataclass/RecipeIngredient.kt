package com.mnvsngv.cookpedia.dataclass

import java.io.Serializable

data class RecipeIngredient(val name: String = "", val quantity: String = "", val serving: String = "") : Serializable