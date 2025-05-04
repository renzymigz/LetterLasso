package cit.edu.letterlasso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class LogoutActivity : Activity() {
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val btnLogout = findViewById<Button>(R.id.button_logout)
        val btnCancel = findViewById<Button>(R.id.button_cancel)

        btnLogout.setOnClickListener {
            // Clear user credentials and login status
            val editor = prefs.edit()
            editor.clear()
            editor.apply()

            Toast.makeText(this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
