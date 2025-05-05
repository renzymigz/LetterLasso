package cit.edu.letterlasso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import cit.edu.letterlasso.util.toast

class ProfilePageActivity : Activity() {
    private lateinit var userPrefs: SharedPreferences
    private lateinit var gamePrefs: SharedPreferences
    private lateinit var closeButton: Button
    private lateinit var saveButton: Button
    private lateinit var userNameField: EditText
    private lateinit var userEmailField: EditText
    private lateinit var userPasswordField: EditText
    private lateinit var currentUserEmail: TextView
    private lateinit var currentPassword: TextView
    private lateinit var levelsUnlockedText: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        gamePrefs = getSharedPreferences("game_progress", Context.MODE_PRIVATE)
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        closeButton = findViewById(R.id.profile_close_button)
        saveButton = findViewById(R.id.save_button)
        userNameField = findViewById(R.id.user_name)
        userEmailField = findViewById(R.id.user_email)
        userPasswordField = findViewById(R.id.password)
        currentUserEmail = findViewById(R.id.current_user_email)
        currentPassword = findViewById(R.id.current_password)
        levelsUnlockedText = findViewById(R.id.levels_unlocked_text)

        // Load and display current user information
        val storedEmail = userPrefs.getString("email", "")
        val storedUsername = userPrefs.getString("username", storedEmail?.substringBefore("@") ?: "User")
        val storedPassword = userPrefs.getString("password", "")

        // Set initial state (view mode)
        updateFieldsState(false)
        saveButton.text = "Edit Profile"

        currentPassword.text = "Password" // Show dots for password
        userNameField.hint = "$storedUsername"
        userEmailField.hint = "Current: $storedEmail"
        userPasswordField.hint = "Enter new password"

        // Calculate and display progress
        val categories = listOf("Animals", "Fruits", "Countries", "Sports", "Food")
        val difficulties = listOf("Easy", "Medium", "Hard")
        var totalLevels = 0
        var unlockedLevels = 0

        for (category in categories) {
            for (difficulty in difficulties) {
                val lastLevel = gamePrefs.getInt("${category}_${difficulty}_last_level", 1)
                unlockedLevels += lastLevel - 1 // Subtract 1 because we start counting from 1
                totalLevels += 5 // Each category has 5 levels per difficulty
            }
        }

        levelsUnlockedText.text = "$unlockedLevels/$totalLevels"

        closeButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Close button now is clicked!")
            this.toast("Heading to landing page!")
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            if (!isEditMode) {
                // Switch to edit mode
                isEditMode = true
                saveButton.text = "Update Profile"
                updateFieldsState(true)
            } else {
                // Save changes and switch back to view mode
                val newEmail = userEmailField.text.toString().trim()
                val newPassword = userPasswordField.text.toString().trim()

                if (validateInputs(newEmail, newPassword)) {
                    // Update SharedPreferences with new values
                    val editor = userPrefs.edit()
                    editor.putString("email", newEmail)
                    if (newPassword.isNotEmpty()) {
                        editor.putString("password", newPassword)
                    }
                    editor.apply()

                    // Update the TextViews with the new values
                    currentUserEmail.text = newEmail
                    this.toast("Profile updated successfully!")
                    
                    // Switch back to view mode
                    isEditMode = false
                    saveButton.text = "Edit Profile"
                    updateFieldsState(false)
                }
            }
        }
    }

    private fun updateFieldsState(editable: Boolean) {
        userNameField.isEnabled = editable
        userEmailField.isEnabled = editable
        userPasswordField.isEnabled = editable
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

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.toast("Enter a valid email!")
            return false
        }
        if (password.isNotEmpty() && password.length < 6) {
            this.toast("Password must be at least 6 characters!")
            return false
        }
        return true
    }
}
