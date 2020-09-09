package com.nemanjamiseljic.circleblue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemanjamiseljic.circleblue.retrofitclasses.JsonPlaceHolderClass
import com.nemanjamiseljic.circleblue.retrofitclasses.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val passPersonDetailsToActivity = "com.nemanjamiseljic.circleblue.passPersonDetails"
class MainScreen : AppCompatActivity(),RecyclerViewPeople.OpenDetails {
    private lateinit var recyclerView:RecyclerView
    private lateinit var retrofit: Retrofit
    private lateinit var recyclerViewPeople: RecyclerViewPeople
    private lateinit var progressBar: ProgressBar
    private lateinit var errorMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        errorMessage = findViewById(R.id.ams_error_message_tv)
        errorMessage.visibility = View.INVISIBLE
        progressBar = findViewById(R.id.ams_progressBar)
        recyclerView = findViewById(R.id.main_screen_recyclerview)
        recyclerViewPeople = RecyclerViewPeople()
        recyclerViewPeople.setInterface(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainScreen)
            hasFixedSize()
            adapter = recyclerViewPeople
        }
        retrofit = Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderClass = retrofit.create(JsonPlaceHolderClass::class.java)

        val call: Call<PeopleList> = jsonPlaceHolderClass.getPeopleListObject()
        call.enqueue(object : Callback<PeopleList> {
            override fun onResponse(call: Call<PeopleList>, response: Response<PeopleList>) {
                if(!response.isSuccessful){
                    println("RETROFIT ERROR CODE : ${response.code()}")
                }else{
                    println("RETROFIT IS SUCCESSFUL CODE : ${response.code()}")

                    val peopleList : PeopleList = response.body() as PeopleList
                    recyclerViewPeople.setListOfPeople(peopleList.results as ArrayList<Result>)
                    progressBar.visibility = View.INVISIBLE

                }
            }

            override fun onFailure(call: Call<PeopleList>, t: Throwable) {
                println("RETROFIT FAILURE ${t.message}")
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = getString(R.string.error_message)

            }
        })
    }

    override fun openDetailsForHero(result: Result) {
        val intent = Intent(this, PersonDetails::class.java)
        intent.putExtra(passPersonDetailsToActivity,result)
        startActivity(intent)
    }
}