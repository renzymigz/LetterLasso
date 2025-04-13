package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cit.edu.letterlasso.fragments.BottomNavFragment

class AnimalsEasyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animals_easy)

        val levelsContainer = findViewById<LinearLayout>(R.id.levels_container)
        
        // Create 5 levels
        for (i in 1..5) {
            val levelButton = Button(this).apply {
                text = "Level $i"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                setOnClickListener {
                    // TODO: Launch the game activity with the specific level
                    val intent = Intent(this@AnimalsEasyActivity, GameActivity::class.java)
                    intent.putExtra("category", "Animals")
                    intent.putExtra("difficulty", "Easy")
                    intent.putExtra("level", i)
                    startActivity(intent)
                }
            }
            levelsContainer.addView(levelButton)
        }

        // Add the bottom navigation fragment
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
        fragmentTransaction.commit()
    }
} 