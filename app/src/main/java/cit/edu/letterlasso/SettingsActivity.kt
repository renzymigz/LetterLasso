package cit.edu.letterlasso

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import cit.edu.letterlasso.util.toast

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val closeButton = findViewById<Button>(R.id.settings_close_button)

        val logoutOption = findViewById<TextView>(R.id.button_signout)
        logoutOption.setOnClickListener {
            showLogoutConfirmation()
        }

        val aboutDevelopers = findViewById<TextView>(R.id.button_aboutus)
        aboutDevelopers.setOnClickListener {
            val intent = Intent(this, DevelopersActivity::class.java)
            startActivity(intent)
        }

        closeButton.setOnClickListener {
            Log.e("LetterLasso", "Profile button now is clicked!")
            //this.toast("Heading to landing page!")
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Oh No!")
            .setMessage("You're signing out, are you sure?")
            .setPositiveButton("YES") { _, _ ->
                Toast.makeText(this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clears back stack
                startActivity(intent)
                finish()
            }
            .setNegativeButton("NO", null)
            .show()
    }
}
