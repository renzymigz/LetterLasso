package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cit.edu.letterlasso.util.toast


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val edittext_username = findViewById<EditText>(R.id.email)
        val edittext_password= findViewById<EditText>(R.id.password)
        val button_login = findViewById<TextView>(R.id.login)
        val button_register = findViewById<TextView>(R.id.register_now)

        intent?.let {
            it.getStringExtra("username")?.let {username ->
                edittext_username.setText(username)
            }

            it.getStringExtra("password")?.let {password ->
                edittext_password.setText(password)
            }
        }

        button_login.setOnClickListener {
            val username = edittext_username.text
            val password = edittext_password.text

            if (username.isEmpty() || password.isEmpty()) {
                this.toast("Username and Password cannot be empty")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                this.toast("Please enter a valid email address")
                return@setOnClickListener
            }

            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }


        button_register.setOnClickListener{
            Log.e("LetterLasso", "Register now is clicked!")
            this.toast("Register now is clicked!")

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



    }
}