package cit.edu.letterlasso

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import cit.edu.letterlasso.fragments.BottomNavFragment

class GameActivity : Activity() {
    private lateinit var wordTextView: TextView
    private lateinit var hangmanImageView: ImageView
    private lateinit var lettersContainer: LinearLayout
    private var currentWord: String = ""
    private var guessedLetters: MutableSet<Char> = mutableSetOf()
    private var remainingGuesses: Int = 6
    private var gameWon: Boolean = false

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
            Log.d("GameActivity", "Views initialized")

            // Get game parameters from intent
            val category = intent.getStringExtra("category") ?: "Animals"
            val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
            val level = intent.getIntExtra("level", 1)
            Log.d("GameActivity", "Category: $category, Difficulty: $difficulty, Level: $level")

            // Set up the game
            setupGame(category, difficulty, level)
            Log.d("GameActivity", "Game setup completed")

            // Add the bottom navigation fragment
            try {
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.bottom_nav_container, BottomNavFragment(), "bottom_nav")
                fragmentTransaction.commit()
                Log.d("GameActivity", "Bottom navigation fragment added")
            } catch (e: Exception) {
                Log.e("GameActivity", "Error adding bottom navigation fragment", e)
            }

            // Set up letter buttons last
            setupLetterButtons()
            Log.d("GameActivity", "Letter buttons setup completed")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in onCreate", e)
            Toast.makeText(this, "Error starting game: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
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

            for (letter in alphabet) {
                if (buttonCount % 7 == 0) {
                    currentRow = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    lettersContainer.addView(currentRow)
                }

                val button = Button(this).apply {
                    text = letter.toString()
                    textSize = 20f
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        setMargins(4, 4, 4, 4)
                    }
                    setOnClickListener {
                        handleLetterGuess(letter)
                        isEnabled = false
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
                updateWordDisplay()
                if (!wordTextView.text.contains("_")) {
                    gameWon = true
                    Toast.makeText(this, "Congratulations! You won!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                // Wrong guess
                remainingGuesses--
                updateHangmanImage()
                if (remainingGuesses <= 0) {
                    Toast.makeText(this, "Game Over! The word was: $currentWord", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in handleLetterGuess", e)
            Toast.makeText(this, "Error processing guess", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWordDisplay() {
        try {
            val displayWord = currentWord.map { letter ->
                if (letter in guessedLetters) letter else '_'
            }.joinToString(" ")
            wordTextView.text = displayWord
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
} 