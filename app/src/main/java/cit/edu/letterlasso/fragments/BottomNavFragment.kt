package cit.edu.letterlasso.fragments

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cit.edu.letterlasso.CategoriesActivity
import cit.edu.letterlasso.HelpPageActivity
import cit.edu.letterlasso.LandingPageActivity
import cit.edu.letterlasso.ProfilePageActivity
import cit.edu.letterlasso.R
import cit.edu.letterlasso.SettingsActivity
import cit.edu.letterlasso.util.toast

class BottomNavFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var buttonClickSound: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences and sound
        sharedPreferences = activity.getSharedPreferences("AppSettings", Activity.MODE_PRIVATE)
        isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)
        buttonClickSound = MediaPlayer.create(activity, R.raw.button_click)

        val homeButton = view.findViewById<ImageView>(R.id.nav_home)
        val profileButton = view.findViewById<ImageView>(R.id.nav_profile)
        val settingsButton = view.findViewById<ImageView>(R.id.nav_settings)
        val helpButton = view.findViewById<TextView>(R.id.nav_help)

        homeButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(activity, LandingPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        }

        profileButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(activity, ProfilePageActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        helpButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(activity, HelpPageActivity::class.java)
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