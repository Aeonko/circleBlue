package com.nemanjamiseljic.circleblue


import com.google.gson.annotations.SerializedName
import com.nemanjamiseljic.circleblue.retrofitclasses.Result

data class PeopleList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<Result>
)