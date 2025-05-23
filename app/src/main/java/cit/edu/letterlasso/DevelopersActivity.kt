package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button

class DevelopersActivity : Activity() {
    private lateinit var btnCancel: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developers)

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)

        val btnCancel = findViewById<Button>(R.id.button_cancel)

        btnCancel.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
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
