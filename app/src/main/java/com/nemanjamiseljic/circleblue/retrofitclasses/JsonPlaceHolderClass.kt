package com.nemanjamiseljic.circleblue.retrofitclasses

import com.nemanjamiseljic.circleblue.PeopleList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface JsonPlaceHolderClass {

    @GET("people/")
    fun  getPeopleListObject(): Call<PeopleList>

    @GET("films/{film_number}")
    fun getFilm(@Path("film_number")movieID:Int):Call<Film>

}