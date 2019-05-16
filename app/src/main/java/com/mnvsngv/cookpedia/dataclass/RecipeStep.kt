package com.mnvsngv.cookpedia.dataclass

import java.io.Serializable

data class RecipeStep(var description: String = "", var imageSource: String = "", val stepNumber: Int = -1) : Serializable