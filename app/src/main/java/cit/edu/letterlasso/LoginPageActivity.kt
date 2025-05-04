package cit.edu.letterlasso

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class LoginPageActivity : Activity() {
    private lateinit var button_login: TextView
    private lateinit var button_register: TextView
    private lateinit var button_back: Button
    private lateinit var edittext_username: EditText
    private lateinit var edittext_password: EditText
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        try {
            // Initialize SharedPreferences
            prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

            // Initialize views
            button_login = findViewById(R.id.login)
            button_register = findViewById(R.id.register_now)
            button_back = findViewById(R.id.back_button)
            edittext_username = findViewById(R.id.email)
            edittext_password = findViewById(R.id.password)

            // Get stored credentials from SharedPreferences
            val storedEmail = prefs.getString("email", "")
            val storedPassword = prefs.getString("password", "")
            edittext_username.setText(storedEmail)
            edittext_password.setText(storedPassword)

            val togglePassword = findViewById<ImageView>(R.id.togglePassword)
            var isPasswordVisible = false

            togglePassword.setOnClickListener {
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

            // Set up click listeners
            button_login.setOnClickListener {
                val username = edittext_username.text.toString()
                val password = edittext_password.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check credentials against stored values
                val storedEmail = prefs.getString("email", "")
                val storedPassword = prefs.getString("password", "")

                if (username == storedEmail && password == storedPassword) {
                    // Credentials match, update login status and go to LandingPage
                    val editor = prefs.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.apply()

                    val intent = Intent(this, LandingPageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }

            button_register.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            button_back.setOnClickListener {
                finish() // Just finish the activity instead of starting LoginActivity again
            }
        } catch (e: Exception) {
            Log.e("LoginPageActivity", "Error in onCreate", e)
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}