package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import cit.edu.letterlasso.fragments.BottomNavFragment

class CategoriesActivity : Activity() {

    private var buttonClickSound: MediaPlayer? = null
    private var itemClickSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)
        itemClickSound = MediaPlayer.create(this, R.raw.button_click)

        val categories = listOf("Animals", "Fruits", "Countries", "Sports", "Food")
        val listView = findViewById<ListView>(R.id.categories_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter

        // Set up back button
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            playSoundEffect(buttonClickSound)
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            playSoundEffect(itemClickSound)
            val intent = Intent(this, DifficultyActivity::class.java)
            intent.putExtra("category", categories[position])
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