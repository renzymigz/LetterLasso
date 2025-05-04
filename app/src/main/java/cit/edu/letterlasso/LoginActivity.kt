package cit.edu.letterlasso

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import cit.edu.letterlasso.util.toast

class LoginActivity : Activity() {

    // added music
    private lateinit var sharedPreferences: SharedPreferences
    private var isMusicEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // added music
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)

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

            button_login.setOnClickListener {
                val intent = Intent(this, LoginPageActivity::class.java)
                startActivity(intent)
            }

            button_register.setOnClickListener {
                Log.e("LetterLasso", "Register now is clicked!")
                this.toast("Register now is clicked!")

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error in onCreate", e)
        }
    }
}