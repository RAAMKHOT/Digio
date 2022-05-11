package com.example.example

import com.google.gson.annotations.SerializedName


data class UploadPdf(

    @SerializedName("signders")
    var signders: ArrayList<Signders> = arrayListOf(),

    @SerializedName("expire_in_days")
    var expireInDays: Int? = null,

    @SerializedName("display_on_page")
    var displayOnPage: String? = null,

    @SerializedName("file_name")
    var fileName: String? = null,

    @SerializedName("file_data")
    var fileData: String? = null

)