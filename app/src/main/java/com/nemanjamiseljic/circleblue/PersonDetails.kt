package com.nemanjamiseljic.circleblue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.nemanjamiseljic.circleblue.retrofitclasses.Film
import com.nemanjamiseljic.circleblue.retrofitclasses.JsonPlaceHolderClass
import com.nemanjamiseljic.circleblue.retrofitclasses.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class PersonDetails : AppCompatActivity() {


    private lateinit var moviesTitle: TextView
    private lateinit var moviesTv: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var retrofit: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_details)
        progressBar = findViewById(R.id.apd_progress_bar)
        getPassedData()
    }

    private fun getPassedData() {
        val result: Result = intent.getSerializableExtra(passPersonDetailsToActivity) as Result
        findViewById<TextView>(R.id.apd_person_name).text = result.name
        findViewById<TextView>(R.id.apd_birth_year).text = result.birthYear
        findViewById<TextView>(R.id.apd_eye_color).text = result.eyeColor
        findViewById<TextView>(R.id.apd_gender).text = result.gender
        findViewById<TextView>(R.id.apd_hair_color).text = result.hairColor
        findViewById<TextView>(R.id.apd_height).text = result.height
        findViewById<TextView>(R.id.apd_mass).text = result.mass
        findViewById<TextView>(R.id.apd_skin_color).text = result.skinColor
        moviesTitle = findViewById(R.id.apd_movies_title)
        moviesTv = findViewById(R.id.apd_movies)
        moviesTitle.visibility = View.INVISIBLE
        moviesTv.visibility = View.INVISIBLE

        loadMovies(result)

    }

    private fun loadMovies(result:Result){
        val stringBuilder = StringBuilder()
        GlobalScope.launch {
            retrofit = Retrofit.Builder()
                .baseUrl("https://swapi.dev/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val jsonPlaceHolderClass = retrofit.create(JsonPlaceHolderClass::class.java)

            val listOfFilmURLs: List<String> = result.films

                listOfFilmURLs.forEachIndexed { index, s ->
                val movieIntValue = Character.getNumericValue(s[s.length-2])

                val call: Call<Film> = jsonPlaceHolderClass.getFilm(movieIntValue)
                    val response: Response<Film> = call.execute()

                    if(!response.isSuccessful){
                        println("RETROFIT ERROR CODE : ${response.code()}")
                    }else{
                        println("RETROFIT IS SUCCESSFUL CODE : ${response.code()}")
                        val film = response.body() as Film
                        stringBuilder.append(film.title+" "+getString(R.string.episode)+" "+film.episodeId+"\n")
                    }
            }

            runOnUiThread {
                moviesTitle.visibility = View.VISIBLE
                moviesTv.visibility = View.VISIBLE
                moviesTv.text = stringBuilder.toString()
                progressBar.visibility = View.INVISIBLE
            }

        }
    }
}