package cit.edu.letterlasso

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ImageView
import cit.edu.letterlasso.util.toast

class LoginActivity : Activity() {

    // added music and sound
    private lateinit var sharedPreferences: SharedPreferences
    private var isMusicEnabled = true
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // added music and sound
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        // Start the MusicService if music is enabled
        if (isMusicEnabled) {
            startService(Intent(this, MusicService::class.java))
        }

        try {
            val logoContainer = findViewById<View>(R.id.logoContainer)

            val floatAnim = ObjectAnimator.ofFloat(logoContainer, "translationY", 0f, -10f, 0f).apply {
                duration = 2000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }

            floatAnim.start()

            val button_login = findViewById<TextView>(R.id.login)
            val button_register = findViewById<TextView>(R.id.register_now)
            val loginGoogle = findViewById<View>(R.id.login_google) // Using View as it's a LinearLayout in XML
            val loginFacebook = findViewById<View>(R.id.login_facebook) // Using View as it's a LinearLayout in XML

            button_login.setOnClickListener {
                playSoundEffect(buttonClickSound)
                val intent = Intent(this, LoginPageActivity::class.java)
                startActivity(intent)
            }

            button_register.setOnClickListener {
                playSoundEffect(buttonClickSound)

                Log.e("LetterLasso", "Register now is clicked!")
                this.toast("Register now is clicked!")

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            loginGoogle.setOnClickListener {
                playSoundEffect(buttonClickSound)
                // handle Google login logic here
                Log.e("LetterLasso", "Login with Google clicked!")
                this.toast("Login with Google clicked!")
            }

            loginFacebook.setOnClickListener {
                playSoundEffect(buttonClickSound)
                // handle Facebook login logic here
                Log.e("LetterLasso", "Login with Facebook clicked!")
                this.toast("Login with Facebook clicked!")
            }

        } catch (e: Exception) {
            Log.e("LoginActivity", "Error in onCreate", e)
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