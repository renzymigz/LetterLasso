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

class NextLevelFragment : Fragment() {

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
    ): View {
        val view = inflater.inflate(R.layout.fragment_next_level, container, false)

        val messageTextView = view.findViewById<TextView>(R.id.message_text_view)
        val nextLevelButton = view.findViewById<Button>(R.id.next_level_button)
        val returnButton = view.findViewById<Button>(R.id.return_button)

        messageTextView.text = "WELL DONE!"

        nextLevelButton.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java).apply {
                putExtra("category", category)
                putExtra("difficulty", difficulty)
                putExtra("level", level) // level is already incremented when fragment was created
            }
            startActivity(intent)
            activity?.finish() // Close the current activity when starting the new one
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
        fun newInstance(category: String, difficulty: String, level: Int): NextLevelFragment {
            val fragment = NextLevelFragment()
            val args = Bundle()
            args.putString("category", category)
            args.putString("difficulty", difficulty)
            args.putInt("level", level)
            fragment.arguments = args
            return fragment
        }
    }
}