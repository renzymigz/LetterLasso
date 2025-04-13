package cit.edu.letterlasso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cit.edu.letterlasso.R
import cit.edu.letterlasso.data.Difficulty

class DifficultyAdapter(private val category: String) : BaseAdapter() {
    private val difficulties = listOf(
        Difficulty("Easy", "Simple words, more attempts", true),
        Difficulty("Medium", "Moderate difficulty words", true),
        Difficulty("Hard", "Complex words, fewer attempts", false)
    )

    override fun getCount(): Int = difficulties.size

    override fun getItem(position: Int): Any = difficulties[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_difficulty, parent, false)

        val difficulty = difficulties[position]
        
        view.findViewById<TextView>(R.id.difficulty_name).text = difficulty.name
        view.findViewById<TextView>(R.id.difficulty_description).text = difficulty.description
        
        val lockIcon = view.findViewById<ImageView>(R.id.lock_icon)
        lockIcon.visibility = if (difficulty.isUnlocked) View.GONE else View.VISIBLE

        return view
    }
} 