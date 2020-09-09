package com.nemanjamiseljic.circleblue

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val randomNumberGeneratorActivity = "com.nemanjamiseljic.circleblue.rngextra"
const val savedStateText = "com.nemanjamiseljic.circleblue.rngsavedStateText"
class RandomNumberGenerator : AppCompatActivity() {
    lateinit var textView:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_number_generator)
        textView = findViewById(R.id.arng_text_view)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putCharSequence(savedStateText, textView.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textView.text = savedInstanceState.getCharSequence(savedStateText)
    }

    fun generateRandomNumber(view: View){
        textView.text =  (10..99).shuffled().first().toString()
    }

    fun getBackToMainScreen(view: View){
        val intent:Intent = Intent()
        intent.putExtra(randomNumberGeneratorActivity, textView.text)
        setResult(RESULT_OK, intent)
        finish()
    }
}