package com.nemanjamiseljic.circleblue

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.StringBuilder
import java.util.*

const val  activity_main_text_put_extra = "com.nemanjamiseljic.circleblue.acmainconst"
const val request_intent_code = 125
const val sharedPreferencesLanguageKey = "com.nemanjamiseljic.circleblue.preferred_language"
const val sharedPreferencesLanguageDefaultValue  = "deffaultvalue"

const val saveInstanceStateMessage = "com.nemanjamiseljic.circleblue.errorMessage"
const val saveInstanceStateBooleanValue = "com.nemanjamiseljic.circleblue.boolvalue"
class MainActivity : AppCompatActivity() {

    private lateinit var chosenLanguage:String

    private lateinit var editText:EditText
    private lateinit var currentDefaultSystemTextColor: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        getSetLanguageFromSP()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(::chosenLanguage.isInitialized){
            findViewById<Button>(R.id.am_lang_button).text = chosenLanguage
        }
        currentDefaultSystemTextColor = textView_lenght.textColors

        editText = findViewById(R.id.activity_main_text_field)
        textView_question_mark.setTextColor(Color.GREEN)
        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               textChangedTriggered()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }


    fun  loginButtonClicked(view: View){
        checkForLogInValidity(editText.text.toString())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun textChangedTriggered(){
        if(editText.text.length in 5..13){
            imageview_lenght.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24))
            textView_lenght.setTextColor(Color.GREEN)
        }else{
            imageview_lenght.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24))
            textView_lenght.setTextColor(currentDefaultSystemTextColor)
        }
        if(checkForTwoOrMoreCharsA(editText.text.toString())){
            imageView_letter_a.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24))
            textView_letter_a.setTextColor(Color.GREEN)
        }else{
            imageView_letter_a.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24))
            textView_letter_a.setTextColor(currentDefaultSystemTextColor)
        }
        if(checkForCharSeven(editText.text.toString())){
            imageView_char_seven.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24))
            textView_char_seven.setTextColor(Color.GREEN)
        }else{
            imageView_char_seven.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24))
            textView_char_seven.setTextColor(currentDefaultSystemTextColor)
        }
        if(!editText.text.contains("?")){
            imageView_question_mark.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24))
            textView_question_mark.setTextColor(Color.GREEN)
        }else{
            imageView_question_mark.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24))
            textView_question_mark.setTextColor(currentDefaultSystemTextColor)
        }
        if(checkForNumberBeforeB(editText.text.toString())){
            imageView_contains_char_b.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_24))
            textView_contains_char_b.setTextColor(Color.GREEN)
        }else{
            imageView_contains_char_b.setImageDrawable(getDrawable(R.drawable.ic_baseline_close_24))
            textView_contains_char_b.setTextColor(currentDefaultSystemTextColor)
        }
    }

    private fun openMainScreen(){
        val intent = Intent(this,MainScreen::class.java)
        startActivity(intent)
    }
    private  fun checkForLogInValidity(logInText: String){
        /**Created delay in coroutines in order to simulate retrieving data from server**/

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        editText.isEnabled = false
        startDialogValidating("",false)
        GlobalScope.launch {
            delay(1500)
            if(logInText.length in 5..13&&
                checkForTwoOrMoreCharsA(logInText)&&
                checkForCharSeven(logInText)&&
                !logInText.contains('?')&&
                checkForNumberBeforeB(logInText)
                    ){
                runOnUiThread(Runnable {
                    Toast.makeText(this@MainActivity, "Log in valid", Toast.LENGTH_SHORT).show()
                    dismissDialogValidating()
                    openMainScreen()
                })

            }else{
                val stringBuilder = StringBuilder()
                stringBuilder.append(getString(R.string.what_went_wrong)).append("\n")
                if(logInText.length !in 5..13){
                    val string: String  = getString(R.string.range_4_14)
                    stringBuilder.append(string).append("\n")
                }
                if(!checkForTwoOrMoreCharsA(logInText)){
                    val string: String  = getString(R.string.letter_a_deosnt_appear)
                    stringBuilder.append(string).append("\n")
                }
                if(!logInText.contains('7')){
                    val string: String  = getString(R.string.doesnt_contain_number7)
                    stringBuilder.append(string).append("\n")
                }
                if (!checkForNumberBeforeB(logInText)){
                    val string: String  = getString(R.string.before_b_cant_be_a_number)
                    stringBuilder.append(string).append("\n")
                }
                if(logInText.contains('?')){
                    val string: String  = getString(R.string.shouldnt_contain_char_question_mark)
                    stringBuilder.append(string).append("\n")
                }

                val sting : String = stringBuilder.toString()
                errorDialogMessage = sting

                runOnUiThread(Runnable {
                    showErrorInDialogValidating(sting)
                })
            }
            runOnUiThread(Runnable {
                editText.isEnabled = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                dismissDialogValidating()
            })

        }
    }


    private fun checkForCharSeven(check:String):Boolean{
        val number = check.count{check.contains("7")}
        return number>0
    }

    private fun checkForTwoOrMoreCharsA(check: String):Boolean{
        var countNumber : Int = 0
        check.forEach {
            if(it == 'a' || it=='A'){
                countNumber++
            }
        }
        return countNumber>=2
    }
    private fun checkForNumberBeforeB(check: String):Boolean{
        /**Returns true if it passes evaluation**/
        if(check.contains("b")){
            check.forEachIndexed { index, c ->
                if(c == 'b'){
                    if(index != 0){
                        val char = check[index - 1]
                        if(char.isDigit()){
                            return false
                        }
                    }
                }
            }
            return true
        }else{
            return false
        }

    }

    private fun getSetLanguageFromSP(){
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        val defaultValue = sharedPreferencesLanguageDefaultValue
        val language = sharedPref.getString(sharedPreferencesLanguageKey, defaultValue)

        if(language!=defaultValue&&language!=null){
            setLocale(language, true)
            chosenLanguage = language
        }
    }

    private fun writeLanguageToSP(newLangValue: String){
        val sharedPref = this?.getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(sharedPreferencesLanguageKey, newLangValue)
            apply()
        }
    }

    fun setLocaleLanguageButton(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.pick_language))
        val animals = arrayOf(getString(R.string.language_english), getString(R.string.language_croatian))
        builder.setItems(
            animals
        ) { dialog, which ->
            when (which) {
                0-> {
                    setLocale("en",false)
                    writeLanguageToSP("en")
                }
                1-> {
                    setLocale("hr",false)
                    writeLanguageToSP("hr")
                }
                 else->{
                     Toast.makeText(this, getString(R.string.something_went_wrong_language_not_changed), Toast.LENGTH_SHORT).show()
                 }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setLocale(localeValue: String, onStart: Boolean){
        val locale = Locale(localeValue)
        Locale.setDefault(locale)

        val res = applicationContext.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        applicationContext.createConfigurationContext(config)
        if(!onStart){
            finish()
            startActivity(intent)
            Toast.makeText(this, "Language updated !", Toast.LENGTH_SHORT).show()
        }

    }

    private lateinit var dialogValidating: Dialog
    private fun startDialogValidating(errorMessage: String,restoreState: Boolean){
        showDialogValidateAfterRestoreState = true
        dialogValidating = Dialog(this)
        dialogValidating.setContentView(R.layout.dialog_validating)
        val window = dialogValidating.window
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

        if(restoreState){
            val textViewPleaseWait : TextView = dialogValidating.findViewById(R.id.dv_please_wait_tv)
            textViewPleaseWait.visibility = View.GONE
            val textViewErrorMessage : TextView = dialogValidating.findViewById(R.id.dv_error_message)
            textViewErrorMessage.visibility = View.VISIBLE
            textViewErrorMessage.text = errorMessage
            val progressBar: ProgressBar = dialogValidating.findViewById(R.id.dv_progressBar)
            progressBar.visibility = View.GONE
            val button:Button = dialogValidating.findViewById(R.id.dv_button_try_again)
            button.visibility = View.VISIBLE
            val cardView: CardView = dialogValidating.findViewById(R.id.dv_cardview)
            cardView.visibility = View.VISIBLE
            button.setOnClickListener {
                showDialogValidateAfterRestoreState = false
                dialogValidating.dismiss()
            }
        }else{
            val textViewErrorMessage : TextView = dialogValidating.findViewById(R.id.dv_error_message)
            textViewErrorMessage.visibility = View.INVISIBLE
            val button:Button = dialogValidating.findViewById(R.id.dv_button_try_again)
            button.visibility = View.INVISIBLE
            val cardView: CardView = dialogValidating.findViewById(R.id.dv_cardview)
            cardView.visibility = View.INVISIBLE
        }

        dialogValidating.setCancelable(false)
        dialogValidating.show()
    }
    private fun dismissDialogValidating(){
        if(::dialogValidating.isInitialized){
            showDialogValidateAfterRestoreState = false
            dialogValidating.dismiss()
        }
    }
    private fun showErrorInDialogValidating(showErrorMessage : String){
        if(::dialogValidating.isInitialized){
            val textViewPleaseWait : TextView = dialogValidating.findViewById(R.id.dv_please_wait_tv)
            textViewPleaseWait.visibility = View.GONE
            val textViewErrorMessage : TextView = dialogValidating.findViewById(R.id.dv_error_message)
            textViewErrorMessage.visibility = View.VISIBLE
            textViewErrorMessage.text = showErrorMessage
            val progressBar: ProgressBar = dialogValidating.findViewById(R.id.dv_progressBar)
            progressBar.visibility = View.GONE
            val button:Button = dialogValidating.findViewById(R.id.dv_button_try_again)
            button.visibility = View.VISIBLE
            val cardView: CardView = dialogValidating.findViewById(R.id.dv_cardview)
            cardView.visibility = View.VISIBLE
            button.setOnClickListener {
                showDialogValidateAfterRestoreState = false
                dialogValidating.dismiss()
            }


        }
    }


    private lateinit var errorDialogMessage: String
    private var showDialogValidateAfterRestoreState:Boolean = false
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val showErrorDialog = savedInstanceState.getBoolean(saveInstanceStateBooleanValue)
        if(showErrorDialog){
            savedInstanceState.getString(saveInstanceStateMessage)?.let {
                startDialogValidating(it,showErrorDialog)
                errorDialogMessage = it
                showDialogValidateAfterRestoreState = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(::errorDialogMessage.isInitialized){
            outState.putCharSequence(saveInstanceStateMessage, errorDialogMessage)
            outState.putBoolean(saveInstanceStateBooleanValue,showDialogValidateAfterRestoreState)
            if (::dialogValidating.isInitialized&&dialogValidating.isShowing){
                dialogValidating.dismiss()
            }
        }
        super.onSaveInstanceState(outState)
    }

    fun rngButtonClicked(view: View){
        val intent: Intent = Intent(this, RandomNumberGenerator::class.java)
        intent.putExtra(activity_main_text_put_extra, editText.text.toString())
        startActivityForResult(intent, request_intent_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==request_intent_code&&resultCode== RESULT_OK){
            if (data != null) {
                editText.setText(data.getStringExtra(randomNumberGeneratorActivity))
            }
        }
    }
}