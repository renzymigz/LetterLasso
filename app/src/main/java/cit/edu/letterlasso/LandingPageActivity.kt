package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LandingPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val profile_button = findViewById<ImageView>(R.id.nav_profile)
        val settings_button = findViewById<ImageView>(R.id.nav_settings)
        val help_button = findViewById<TextView>(R.id.nav_help)
        val play_button = findViewById<Button>(R.id.play_button)

        play_button.setOnClickListener {
            Log.e("LetterLasso", "Play button clicked!")
            Toast.makeText(this, "Starting game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }

        profile_button.setOnClickListener{
            Log.e("LetterLasso", "Profile button now is clicked!")
            Toast.makeText(this, "Heading to profile page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
        }

        settings_button.setOnClickListener{
            Log.e("LetterLasso", "Settings button now is clicked!")
            Toast.makeText(this, "Heading to settings page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        help_button.setOnClickListener{
            Log.e("LetterLasso", "Help button now is clicked!")
            Toast.makeText(this, "Heading to help page!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, HelpPageActivity::class.java)
            startActivity(intent)
        }
    }
}