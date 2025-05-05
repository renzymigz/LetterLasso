package cit.edu.letterlasso

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cit.edu.letterlasso.util.toast

class SettingsActivity : Activity() {

    // modified part
    private var isSoundEnabled = true
    private var isMusicEnabled = true

    private lateinit var soundIcon: ImageView
    private lateinit var musicIcon: ImageView

    private lateinit var closeButton: Button
    private lateinit var logoutOption: TextView
    private lateinit var aboutDevelopers: TextView
    private lateinit var policyButton: Button
    private lateinit var termsButton: Button

    private lateinit var sharedPreferences: SharedPreferences
    private var buttonClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // modified part
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)

        soundIcon = findViewById(R.id.sound_icon)
        musicIcon = findViewById(R.id.music_icon)
        closeButton = findViewById(R.id.settings_close_button)
        logoutOption = findViewById(R.id.button_signout)
        aboutDevelopers = findViewById(R.id.button_aboutus)
        policyButton = findViewById(R.id.button_policy)
        termsButton = findViewById(R.id.button_terms)

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click) // Initialize button click sound

        updateSoundUI()
        updateMusicUI()

        soundIcon.setOnClickListener {
            isSoundEnabled = !isSoundEnabled
            updateSoundUI()
            playSoundEffect(buttonClickSound) // Play sound on sound icon click
            saveSettings()
        }

        musicIcon.setOnClickListener {
            isMusicEnabled = !isMusicEnabled
            updateMusicUI()
            toggleBackgroundMusicService()
            playSoundEffect(buttonClickSound) // Play sound on music icon click
            saveSettings()
            Log.d("SettingsActivity", "Music toggle clicked, starting service: $isMusicEnabled")
        }

        logoutOption.setOnClickListener {
            playSoundEffect(buttonClickSound) // Play sound on music icon click
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }

        aboutDevelopers.setOnClickListener {
            playSoundEffect(buttonClickSound) // Play sound on music icon click
            val intent = Intent(this, DevelopersActivity::class.java)
            startActivity(intent)
        }

        closeButton.setOnClickListener {
            Log.e("LetterLasso", "Profile button now is clicked!")
            playSoundEffect(buttonClickSound) // Play sound on music icon click
            this.toast("Heading to landing page!")
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        policyButton.setOnClickListener {
            playSoundEffect(buttonClickSound) // Play sound on policy button click
            Toast.makeText(this, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        termsButton.setOnClickListener {
            playSoundEffect(buttonClickSound) // Play sound on terms button click
            Toast.makeText(this, "Terms and Conditions Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            startActivity(intent)
        }

        // Start the music service if enabled on creation
        if (isMusicEnabled) {
            startService(Intent(this, MusicService::class.java))
        }
    }

    private fun playSoundEffect(mediaPlayer: MediaPlayer?) {
        if (isSoundEnabled && mediaPlayer != null) {
            mediaPlayer.start()
        }
    }

    private fun toggleBackgroundMusicService() {
        val serviceIntent = Intent(this, MusicService::class.java)
        if (isMusicEnabled) {
            startService(serviceIntent)
            Log.d("SettingsActivity", "Starting MusicService")
        } else {
            stopService(serviceIntent)
            Log.d("SettingsActivity", "Stopping MusicService")
        }
    }

    private fun updateSoundUI() {
        if (isSoundEnabled) {
            soundIcon.setImageResource(R.drawable.volume) // Sound on icon
        } else {
            soundIcon.setImageResource(R.drawable.volume_mute) // Sound off icon
        }
    }

    private fun updateMusicUI() {
        if (isMusicEnabled) {
            musicIcon.setImageResource(R.drawable.music) // Music on icon
        } else {
            musicIcon.setImageResource(R.drawable.music_off) // Music off icon
        }
    }

    private fun saveSettings() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSoundEnabled", isSoundEnabled)
        editor.putBoolean("isMusicEnabled", isMusicEnabled)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonClickSound?.release()
        buttonClickSound = null
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
