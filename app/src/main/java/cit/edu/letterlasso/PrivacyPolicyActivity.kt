package cit.edu.letterlasso

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button

class PrivacyPolicyActivity : Activity() {

    private var buttonClickSound: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        val closeButton = findViewById<Button>(R.id.button_close_policy)

        closeButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            finish()
        }
    }

    private fun playSoundEffect(mediaPlayer: MediaPlayer?) {
        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
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