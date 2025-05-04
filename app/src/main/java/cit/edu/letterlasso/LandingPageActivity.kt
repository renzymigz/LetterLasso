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
import cit.edu.letterlasso.fragments.BottomNavFragment

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


        val play_button = findViewById<Button>(R.id.play_button)


        play_button.setOnClickListener {
            playSoundEffect(buttonClickSound)
            Log.e("LetterLasso", "Play button clicked!")
            Toast.makeText(this, "Starting game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }

        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
        fragmentTransaction.commit()



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