package cit.edu.letterlasso

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.*
import cit.edu.letterlasso.util.toast
import cit.edu.letterlasso.util.txt
import java.util.Calendar

class RegisterActivity : Activity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        val edittext_username = findViewById<EditText>(R.id.email)
        val edittext_password = findViewById<EditText>(R.id.password)
        val edittext_confirmpassword = findViewById<EditText>(R.id.confirm_password)
        val button_back = findViewById<TextView>(R.id.back_button)
        val register_button = findViewById<TextView>(R.id.register)
        val dateInput = findViewById<EditText>(R.id.date_input)
        val accountAlready = findViewById<TextView>(R.id.account_already) // added

        val togglePassword = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirmPassword = findViewById<ImageView>(R.id.toggleConfirmPassword)

        var isPasswordVisible = false
        var isConfirmPasswordVisible = false

        togglePassword.setOnClickListener {
            playSoundEffect(buttonClickSound)
            if (isPasswordVisible) {
                edittext_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_eye_close)
                isPasswordVisible = false
            } else {
                edittext_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_eye_open)
                isPasswordVisible = true
            }
            edittext_password.setSelection(edittext_password.text.length)
        }

        toggleConfirmPassword.setOnClickListener {
            playSoundEffect(buttonClickSound)
            if (isConfirmPasswordVisible) {
                edittext_confirmpassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggleConfirmPassword.setImageResource(R.drawable.ic_eye_close)
                isConfirmPasswordVisible = false
            } else {
                edittext_confirmpassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleConfirmPassword.setImageResource(R.drawable.ic_eye_open)
                isConfirmPasswordVisible = true
            }
            edittext_confirmpassword.setSelection(edittext_confirmpassword.text.length)
        }

        dateInput.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format date and set to EditText
                val formattedDate = "${selectedDay.toString().padStart(2, '0')}/${(selectedMonth + 1).toString().padStart(2, '0')}/$selectedYear"
                dateInput.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        register_button.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val username = edittext_username.txt()
            val password = edittext_password.txt()
            val confirmpassword = edittext_confirmpassword.txt()
            val birthdate = dateInput.txt()

            if (username.isNullOrEmpty() || password.isNullOrEmpty() || birthdate.isNullOrEmpty()) {
                this.toast("Email, Password and Birthdate cannot be empty")
                return@setOnClickListener
            }

            if(password != confirmpassword){
                this.toast("Mismatch password")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                this.toast("Please enter a valid email address")
                return@setOnClickListener
            }

            // Check if email already exists
            val existingEmail = prefs.getString("email", "")
            if (existingEmail == username) {
                this.toast("This email is already registered")
                return@setOnClickListener
            }

            // Save user credentials using SharedPreferences
            val editor = prefs.edit()
            editor.putString("email", username)
            editor.putString("password", password)
            editor.putString("birthdate", birthdate)
            editor.putBoolean("is_logged_in", true)
            editor.apply()

            startActivity(
                Intent(this, LoginPageActivity::class.java)
            )
        }

        accountAlready.setOnClickListener {
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Already have an account now is clicked!")
            this.toast("Already have an account now is clicked!")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val button_login = findViewById<TextView>(R.id.account_already)
        button_login.setOnClickListener {
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Already have an account now is clicked!")
            this.toast("Already have an account now is clicked!")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        button_back.setOnClickListener{
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Back button is clicked!")
            this.toast("Back button is clicked!")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playSoundEffect(mediaPlayer: MediaPlayer?) {
        if (isSoundEnabled && mediaPlayer != null) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonClickSound?.release()
        buttonClickSound = null
    }
}
