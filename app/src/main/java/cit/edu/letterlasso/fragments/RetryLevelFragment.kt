package cit.edu.letterlasso.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import cit.edu.letterlasso.DifficultyActivity
import cit.edu.letterlasso.GameActivity
import cit.edu.letterlasso.R

class RetryLevelFragment : Fragment() {

    private var category: String? = null
    private var difficulty: String? = null
    private var level: Int = 1
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString("category")
            difficulty = it.getString("difficulty")
            level = it.getInt("level", 1)
        }
        prefs = activity.getSharedPreferences("game_progress", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_retry_level, container, false)

        val retryMessageTextView = view.findViewById<TextView>(R.id.retry_message_text_view)
        val retryButton = view.findViewById<Button>(R.id.retry_level_button)
        val returnButton = view.findViewById<Button>(R.id.return_button)

        retryMessageTextView.text = "TRY AGAIN!"

        retryButton.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java).apply {
                putExtra("category", category)
                putExtra("difficulty", difficulty)
                putExtra("level", level) // retry the same level
            }
            startActivity(intent)
            activity?.finish() // Close the current activity when retrying the level
        }

        returnButton.setOnClickListener {
            // Save the current level as the last checkpoint
            val editor = prefs.edit()
            editor.putInt("${category}_${difficulty}_last_level", level)
            editor.apply()

            // Return to difficulty page
            val intent = Intent(activity, DifficultyActivity::class.java).apply {
                putExtra("category", category)
            }
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    companion object {
        fun newInstance(category: String, difficulty: String, level: Int): RetryLevelFragment {
            val fragment = RetryLevelFragment()
            val args = Bundle()
            args.putString("category", category)
            args.putString("difficulty", difficulty)
            args.putInt("level", level)
            fragment.arguments = args
            return fragment
        }
    }
}
