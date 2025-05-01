package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import cit.edu.letterlasso.fragments.BottomNavFragment
import cit.edu.letterlasso.fragments.NextLevelFragment
import cit.edu.letterlasso.fragments.RetryLevelFragment

class GameActivity : Activity() {
    private lateinit var wordTextView: TextView
    private lateinit var hangmanImageView: ImageView
    private lateinit var lettersContainer: LinearLayout
    private lateinit var levelTitleTextView: TextView
    private var currentWord: String = ""
    private var guessedLetters: MutableSet<Char> = mutableSetOf()
    private var remainingGuesses: Int = 6
    private var gameWon: Boolean = false

    // added for music
    private lateinit var sharedPreferences: SharedPreferences
    private var isSoundEnabled = true
    private var correctGuessSound: MediaPlayer? = null
    private var wrongGuessSound: MediaPlayer? = null
    private var levelCompleteSound: MediaPlayer? = null
    private var gameOverSound: MediaPlayer? = null
    private var buttonClickSound: MediaPlayer? = null // added button click sound

    // Sample words for different categories and difficulties
    private val animalWords = mapOf(
        "Easy" to listOf("CAT", "DOG", "BIRD", "FISH", "DUCK"),
        "Medium" to listOf("ELEPHANT", "GIRAFFE", "PENGUIN", "DOLPHIN", "KANGAROO"),
        "Hard" to listOf("RHINOCEROS", "OCTOPUS", "CHAMELEON", "PENGUIN", "KANGAROO")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d("GameActivity", "onCreate started")

            setContentView(R.layout.activity_game)
            Log.d("GameActivity", "setContentView completed")

            // Initialize views
            wordTextView = findViewById(R.id.word_text_view)
            hangmanImageView = findViewById(R.id.hangman_image_view)
            lettersContainer = findViewById(R.id.letters_container)
            levelTitleTextView = findViewById(R.id.level_title)
            Log.d("GameActivity", "Views initialized")

            // Load sound setting
            sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
            isSoundEnabled = sharedPreferences.getBoolean("isSoundEnabled", true)

            // Initialize sound effects
            correctGuessSound = MediaPlayer.create(this, R.raw.correct_guess)
            wrongGuessSound = MediaPlayer.create(this, R.raw.wrong_guess)
            levelCompleteSound = MediaPlayer.create(this, R.raw.level_complete)
            gameOverSound = MediaPlayer.create(this, R.raw.game_over)
            buttonClickSound = MediaPlayer.create(this, R.raw.button_click) // Initialize button click sound// Replace with your sound file

            // Get game parameters from intent
            val category = intent.getStringExtra("category") ?: "Animals"
            val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
            val level = intent.getIntExtra("level", 1)
            Log.d("GameActivity", "Category: $category, Difficulty: $difficulty, Level: $level")

            // Update level title
            levelTitleTextView.text = "Level $level"
            levelTitleTextView.setTextColor(Color.parseColor("#ffd493"))
            levelTitleTextView.typeface = ResourcesCompat.getFont(this, R.font.vhs_gothic)

            // Set up the game
            setupGame(category, difficulty, level)
            Log.d("GameActivity", "Game setup completed")

            // Add the bottom navigation fragment
//            try {
//                val fragmentManager = fragmentManager
//                val fragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
//                fragmentTransaction.commit()
//                Log.d("GameActivity", "Bottom navigation fragment added")
//            } catch (e: Exception) {
//                Log.e("GameActivity", "Error adding bottom navigation fragment", e)
//            }

            // Set up letter buttons last
            setupLetterButtons()
            Log.d("GameActivity", "Letter buttons setup completed")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error starting game: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun playSoundEffect(mediaPlayer: MediaPlayer?) {
        if (isSoundEnabled && mediaPlayer != null) {
            mediaPlayer.start()
        }
    }

    private fun setupGame(category: String, difficulty: String, level: Int) {
        try {
            // Get word based on category and difficulty
            currentWord = when (category) {
                "Animals" -> animalWords[difficulty]?.get((level - 1) % 5) ?: "CAT"
                else -> "CAT" // Default word
            }
            Log.d("GameActivity", "Selected word: $currentWord")

            // Initialize the word display
            updateWordDisplay()
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in setupGame", e)
            currentWord = "CAT" // Fallback word
        }
    }

    private fun setupLetterButtons() {
        try {
            val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            var currentRow: LinearLayout? = null
            var buttonCount = 0

            // Create the LinearLayout for buttons
            val rowLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            rowLayoutParams.gravity = Gravity.CENTER // Ensures that the row is centered in its parent

            for (letter in alphabet) {
                if (buttonCount % 7 == 0) {
                    // Create a new row layout
                    currentRow = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = rowLayoutParams
                        gravity = Gravity.CENTER // Ensures the buttons are centered in the row
                    }
                    lettersContainer.addView(currentRow)
                }

                val typeface = ResourcesCompat.getFont(this, R.font.vhs_gothic)

                val button = Button(this).apply {
                    text = letter.toString()
                    textSize = 20f
                    setBackgroundResource(R.drawable.button_background)

                    // Set the button layout
                    typeface?.let{ this.typeface = it}
                    layoutParams = LinearLayout.LayoutParams(130, 130).apply {
                        setMargins(4, 4, 4, 4)
                    }
                    setTextColor(Color.parseColor("#ffd493"))

                    // Center the text inside the button
                    gravity = Gravity.CENTER

                    setOnClickListener {
                        handleLetterGuess(letter)
                        isEnabled = false
                        alpha = 0.5f
                    }
                }

                currentRow?.addView(button)
                buttonCount++
            }
            Log.d("GameActivity", "Created $buttonCount letter buttons")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in setupLetterButtons", e)
            Toast.makeText(this, "Error setting up letter buttons", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLetterGuess(letter: Char) {
        try {
            if (letter in guessedLetters) return
            guessedLetters.add(letter)

            if (letter in currentWord) {
                // Correct guess
                playSoundEffect(correctGuessSound)
                updateWordDisplay()
                if (!wordTextView.text.contains("_")) {
                    gameWon = true
                    Toast.makeText(this, "Level Complete!", Toast.LENGTH_SHORT).show()
                    playSoundEffect(levelCompleteSound) // added

                    val nextLevel = intent.getIntExtra("level", 1) + 1
                    val category = intent.getStringExtra("category") ?: "Animals"
                    val difficulty = intent.getStringExtra("difficulty") ?: "Easy"

                    // Check if all levels are completed
                    if (nextLevel > 5) {
                        // All levels completed, redirect to difficulty page
                        Toast.makeText(this, "Congratulations! You've completed $difficulty difficulty!", Toast.LENGTH_LONG).show()
                        
                        // Save completion status
                        val prefs = getSharedPreferences("game_progress", MODE_PRIVATE)
                        val editor = prefs.edit()
                        when (difficulty) {
                            "Easy" -> editor.putBoolean("${category}_easy_completed", true)
                            "Medium" -> editor.putBoolean("${category}_medium_completed", true)
                        }
                        editor.apply()
                        
                        val intent = Intent(this, DifficultyActivity::class.java)
                        intent.putExtra("category", category)
                        startActivity(intent)
                        finish()
                    } else {
                        // Show NextLevelFragment for next level
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.next_level_container, NextLevelFragment.newInstance(category, difficulty, nextLevel))
                        fragmentTransaction.commit()

                        findViewById<FrameLayout>(R.id.next_level_container).visibility = View.VISIBLE
                    }

                    // Disable all letter buttons since the game is won
                    disableAllLetterButtons()
                }
            } else {
                // Wrong guess
                playSoundEffect(wrongGuessSound) // added sound
                remainingGuesses--
                updateHangmanImage()
                if (remainingGuesses <= 0) {
                    // Game over, show the retry fragment
                    val category = intent.getStringExtra("category") ?: "Animals"
                    val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
                    val level = intent.getIntExtra("level", 1)

                    // Show RetryLevelFragment
                    Toast.makeText(this, "Game Over! The word was: $currentWord", Toast.LENGTH_SHORT).show()
                    playSoundEffect(gameOverSound) // added sound

                    // Show RetryLevelFragment
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.next_level_container, RetryLevelFragment.newInstance(category, difficulty, level))
                    fragmentTransaction.commit()

                    findViewById<FrameLayout>(R.id.next_level_container).visibility = View.VISIBLE

                    // Disable all letter buttons since the game is lost
                    disableAllLetterButtons()
                }
            }
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in handleLetterGuess", e)
            Toast.makeText(this, "Error processing guess", Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableAllLetterButtons() {
        for (i in 0 until lettersContainer.childCount) {
            val row = lettersContainer.getChildAt(i) as? LinearLayout
            row?.let {
                for (j in 0 until it.childCount) {
                    val button = it.getChildAt(j) as? Button
                    button?.let { btn ->
                        btn.isEnabled = false
                        btn.alpha = 0.5f
                    }
                }
            }
        }
    }

    private fun updateWordDisplay() {
        try {
            val displayWord = currentWord.map { letter ->
                if (letter in guessedLetters) letter else '_'
            }.joinToString(" ")
            
            // Create a SpannableString to style the text
            val spannableString = android.text.SpannableString(displayWord)
            
            // Create a custom background drawable with padding
            val backgroundDrawable = android.graphics.drawable.GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 100f
            }
            
            // Fixed width for all boxes
            val fixedBoxWidth = 100 // pixels
            
            // Add background and padding to each character
            var start = 0
            for (i in 0 until displayWord.length) {
                if (displayWord[i] != ' ') {
                    // Create a custom span with padding
                    val span = object : android.text.style.ReplacementSpan() {
                        override fun getSize(
                            paint: android.graphics.Paint,
                            text: CharSequence,
                            start: Int,
                            end: Int,
                            fm: android.graphics.Paint.FontMetricsInt?
                        ): Int {
                            return fixedBoxWidth
                        }

                        override fun draw(
                            canvas: android.graphics.Canvas,
                            text: CharSequence,
                            start: Int,
                            end: Int,
                            x: Float,
                            top: Int,
                            y: Int,
                            bottom: Int,
                            paint: android.graphics.Paint
                        ) {
                            // Draw background with fixed width
                            backgroundDrawable.setBounds(
                                x.toInt(),
                                top - 20,
                                x.toInt() + fixedBoxWidth,
                                bottom + 20
                            )
                            backgroundDrawable.draw(canvas)
                            
                            // Center the text in the box
                            val textWidth = paint.measureText(text, start, end)
                            val textX = x + (fixedBoxWidth - textWidth) / 2
                            canvas.drawText(text, start, end, textX, y.toFloat(), paint)
                        }
                    }
                    
                    spannableString.setSpan(
                        span,
                        start,
                        start + 1,
                        android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                start++
            }
            
            // Set the text with the background
            wordTextView.text = spannableString
            wordTextView.setTextColor(Color.BLACK)
            wordTextView.textSize = 30f
            
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in updateWordDisplay", e)
        }
    }

    private fun updateHangmanImage() {
        try {
            // Update the hangman image based on remaining guesses
            val imageResource = when (remainingGuesses) {
                6 -> R.drawable.hangman_0
                5 -> R.drawable.hangman_1
                4 -> R.drawable.hangman_2
                3 -> R.drawable.hangman_3
                2 -> R.drawable.hangman_4
                1 -> R.drawable.hangman_5
                else -> R.drawable.hangman_6
            }
            hangmanImageView.setImageResource(imageResource)
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in updateHangmanImage", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        correctGuessSound?.release()
        correctGuessSound = null
        wrongGuessSound?.release()
        wrongGuessSound = null
        levelCompleteSound?.release()
        levelCompleteSound = null
        gameOverSound?.release()
        gameOverSound = null
        buttonClickSound?.release() // Release button click sound
        buttonClickSound = null
    }
}