package cit.edu.letterlasso

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LandingPageActivity : Activity() {

    // added sound
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // added sound
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        val logoContainer = findViewById<FrameLayout>(R.id.logoContainer)

        val floatAnim = ObjectAnimator.ofFloat(logoContainer, "translationY", 0f, -10f, 0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        floatAnim.start()

        val profile_button = findViewById<ImageView>(R.id.nav_profile)
        val settings_button = findViewById<ImageView>(R.id.nav_settings)
        val help_button = findViewById<TextView>(R.id.nav_help)
        val play_button = findViewById<Button>(R.id.play_button)
        val difficulty_button = findViewById<ImageView>(R.id.nav_difficulty)

        play_button.setOnClickListener {
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Play button clicked!")
            Toast.makeText(this, "Starting game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }

        profile_button.setOnClickListener{
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Profile button now is clicked!")
            Toast.makeText(this, "Heading to profile page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }

        settings_button.setOnClickListener{
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Settings button now is clicked!")
            Toast.makeText(this, "Heading to settings page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        help_button.setOnClickListener{
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Help button now is clicked!")
            Toast.makeText(this, "Heading to help page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, HelpPageActivity::class.java)
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