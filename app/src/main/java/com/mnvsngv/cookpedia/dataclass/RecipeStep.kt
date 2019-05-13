package com.mnvsngv.cookpedia.dataclass

import java.io.Serializable

//Steps Item dataclass
data class RecipeStep(val description: String = "", val imageSource: String = "", val stepNumber: Int = -1) : Serializable