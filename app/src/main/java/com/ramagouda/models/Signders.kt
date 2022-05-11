package com.example.example

import com.google.gson.annotations.SerializedName


data class Signders(
    @SerializedName("identifier")
    var identifier: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("reason")
    var reason: String? = null

)