package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import cit.edu.letterlasso.adapters.DifficultyAdapter
import cit.edu.letterlasso.fragments.BottomNavFragment

class DifficultyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)

        val category = intent.getStringExtra("category") ?: "Animals"
        val listView = findViewById<ListView>(R.id.difficulty_list)
        listView.adapter = DifficultyAdapter(category)

        // Handle difficulty selection
        listView.setOnItemClickListener { _, _, position, _ ->
            val difficulty = when (position) {
                0 -> "Easy"
                1 -> "Medium"
                2 -> "Hard"
                else -> "Easy"
            }

            // Launch appropriate activity based on category and difficulty
            val intent = when {
                category == "Animals" && difficulty == "Easy" -> Intent(this, AnimalsEasyActivity::class.java)
//                category == "Animals" && difficulty == "Medium" -> Intent(this, AnimalsMediumActivity::class.java)
//                category == "Animals" && difficulty == "Hard" -> Intent(this, AnimalsHardActivity::class.java)
                else -> Intent(this, AnimalsEasyActivity::class.java) // Default fallback
            }

            intent.putExtra("category", category)
            intent.putExtra("difficulty", difficulty)
            startActivity(intent)
        }

        // Add the bottom navigation fragment
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
        fragmentTransaction.commit()
    }
}
