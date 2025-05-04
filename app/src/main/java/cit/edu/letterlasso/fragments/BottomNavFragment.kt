package cit.edu.letterlasso.fragments

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cit.edu.letterlasso.HelpPageActivity
import cit.edu.letterlasso.LandingPageActivity
import cit.edu.letterlasso.ProfilePageActivity
import cit.edu.letterlasso.R
import cit.edu.letterlasso.SettingsActivity

class BottomNavFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val home_button = view.findViewById<ImageView>(R.id.nav_home)
        val profile_button = view.findViewById<ImageView>(R.id.nav_profile)
        val settings_button = view.findViewById<ImageView>(R.id.nav_settings)
        val help_button = view.findViewById<TextView>(R.id.nav_help)

        home_button.setOnClickListener{
            val intent = Intent(activity, LandingPageActivity::class.java)
            startActivity(intent)
        }

        profile_button.setOnClickListener {
            val intent = Intent(activity, ProfilePageActivity::class.java)
            startActivity(intent)
        }

        settings_button.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        help_button.setOnClickListener {
            val intent = Intent(activity, HelpPageActivity::class.java)
            startActivity(intent)
        }
    }
} 