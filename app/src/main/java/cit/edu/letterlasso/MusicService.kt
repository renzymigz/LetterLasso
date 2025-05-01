package cit.edu.letterlasso

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.content.SharedPreferences
import android.util.Log

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isMusicEnabled = true

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "onCreate called")
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music) // Replace with your music file
        mediaPlayer?.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "onStartCommand called")
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true)
        if (isMusicEnabled && mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            Log.d("MusicService", "Music started")
        } else if (!isMusicEnabled && mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
            Log.d("MusicService", "Music paused")
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        const val ACTION_PLAY = "cit.edu.letterlasso.ACTION_PLAY"
        const val ACTION_PAUSE = "cit.edu.letterlasso.ACTION_PAUSE"
    }
}