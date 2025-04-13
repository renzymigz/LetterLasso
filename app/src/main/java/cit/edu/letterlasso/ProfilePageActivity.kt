package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cit.edu.letterlasso.util.toast

class ProfilePageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val closeButton = findViewById<Button>(R.id.profile_close_button)
        val saveButton = findViewById<Button>(R.id.save_button)
        val userNameField = findViewById<EditText>(R.id.user_name)
        val userEmailField = findViewById<EditText>(R.id.user_email)
        val currentUserName = findViewById<TextView>(R.id.current_user_name)
        val currentUserEmail = findViewById<TextView>(R.id.current_user_email)

        closeButton.setOnClickListener {
            Log.e("LetterLasso", "Close button now is clicked!")
            this.toast("Heading to landing page!")
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            val newUsername = userNameField.text.toString().trim()
            val newEmail = userEmailField.text.toString().trim()

            if (validateInputs(newUsername, newEmail)) {
                // Update the TextViews with the new values
                currentUserName.text = newUsername
                currentUserEmail.text = newEmail
                this.toast("Profile updated successfully!")
            }
        }
    }

    private fun validateInputs(username: String, email: String): Boolean {
        if (username.isEmpty()) {
            this.toast("Profile updated successfully!")
            return false
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.toast("Enter a valid email!")
            return false
        }
        return true
    }
}
