package com.mnvsngv.cookpedia.dataclass

import java.io.Serializable

data class RecipeStep(val description: String = "", val imageSource: String = "", val stepNumber: Int = -1) : Serializable