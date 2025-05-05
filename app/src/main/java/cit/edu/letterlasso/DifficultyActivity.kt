package cit.edu.letterlasso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import cit.edu.letterlasso.adapters.DifficultyAdapter
import cit.edu.letterlasso.fragments.BottomNavFragment

class DifficultyActivity : Activity() {
    private lateinit var prefs: SharedPreferences
    private var buttonClickSound: MediaPlayer? = null
    private var itemClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)

        prefs = getSharedPreferences("game_progress", Context.MODE_PRIVATE)
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)
        itemClickSound = MediaPlayer.create(this, R.raw.button_click)

        val category = intent.getStringExtra("category") ?: "Animals"
        val listView = findViewById<ListView>(R.id.difficulty_list)
        listView.adapter = DifficultyAdapter(category, this)

        // Set up back button
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handle difficulty selection
        listView.setOnItemClickListener { _, _, position, _ ->
            playSoundEffect(itemClickSound)
            val difficulty = when (position) {
                0 -> "Easy"
                1 -> "Medium"
                2 -> "Hard"
                else -> "Easy"
            }

            // Check if the difficulty is unlocked
            val isUnlocked = when (difficulty) {
                "Easy" -> true
                "Medium" -> prefs.getBoolean("${category}_easy_completed", false)
                "Hard" -> prefs.getBoolean("${category}_medium_completed", false)
                else -> false
            }

            if (!isUnlocked) {
                Toast.makeText(this, "Complete previous difficulty to unlock!", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            // Get the last level checkpoint for this category and difficulty
            val lastLevel = prefs.getInt("${category}_${difficulty}_last_level", 1)

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("difficulty", difficulty)
            intent.putExtra("level", lastLevel) // Start at the last checkpoint
            startActivity(intent)
        }

        // Add the bottom navigation fragment
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
        fragmentTransaction.commit()
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
        itemClickSound?.release()
        itemClickSound = null
    }
}
