package com.mnvsngv.cookpedia.singleton

import com.mnvsngv.cookpedia.backend.Backend
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.backend.FirebaseBackend

object BackendFactory {
    fun getInstance(listener: BackendListener): Backend {
        return FirebaseBackend(listener)
    }
}