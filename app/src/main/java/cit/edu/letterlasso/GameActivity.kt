package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
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
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class GameActivity : Activity() {
    private lateinit var wordTextView: TextView
    private lateinit var hangmanImageView: ImageView
    private lateinit var lettersContainer: LinearLayout
    private lateinit var levelTitleTextView: TextView
    private lateinit var heartIcon: ImageView
    private lateinit var konfettiView: KonfettiView
    private lateinit var hintTextView: TextView
    private var currentWord: String = ""
    private var guessedLetters: MutableSet<Char> = mutableSetOf()
    private var remainingGuesses: Int = 6
    private var gameWon: Boolean = false

    // Sample words for different categories and difficulties
    private val animalWords = mapOf(
        "Easy" to listOf("CAT", "DOG", "BIRD", "FISH", "DUCK"),
        "Medium" to listOf("ELEPHANT", "GIRAFFE", "PENGUIN", "DOLPHIN", "KANGAROO"),
        "Hard" to listOf("OCTOPUS", "CHAMELEON", "PENGUIN", "KANGAROO", "GIRAFFE")
    )

    private val fruitWords = mapOf(
        "Easy" to listOf("APPLE", "BANANA", "GRAPE", "PEAR", "KIWI"),
        "Medium" to listOf("ORANGE", "PINEAPPLE", "WATERMELON", "STRAWBERRY", "BLUEBERRY"),
        "Hard" to listOf("BLUEBERRY", "BLACKBERRY", "RASPBERRY", "PINEAPPLE", "WATERMELON")
    )

    private val countryWords = mapOf(
        "Easy" to listOf("JAPAN", "CHINA", "INDIA", "EGYPT", "ITALY"),
        "Medium" to listOf("CANADA", "BRAZIL", "GERMANY", "FRANCE", "MEXICO"),
        "Hard" to listOf("THAILAND", "VIETNAM", "MALAYSIA", "SINGAPORE", "CAMBODIA")
    )

    private val sportWords = mapOf(
        "Easy" to listOf("SOCCER", "TENNIS", "GOLF", "SWIM", "RUN"),
        "Medium" to listOf("HOCKEY", "CRICKET", "ARCHERY", "FENCING", "BOXING"),
        "Hard" to listOf("BADMINTON", "WRESTLING", "GYMNASTICS", "FENCING", "ARCHERY")
    )

    private val foodWords = mapOf(
        "Easy" to listOf("PIZZA", "PASTA", "BREAD", "RICE", "CAKE"),
        "Medium" to listOf("BURGER", "SUSHI", "TACOS", "NOODLE", "SALAD"),
        "Hard" to listOf("SPAGHETTI", "LASAGNA", "CURRY", "STEAK", "SANDWICH")
    )

    private val animalHints = mapOf(
        "CAT" to "A small domesticated carnivorous mammal",
        "DOG" to "Man's best friend, known for loyalty",
        "BIRD" to "Feathered creature that can fly",
        "FISH" to "Lives in water and breathes through gills",
        "DUCK" to "Web-footed water bird that quacks",
        "ELEPHANT" to "Largest land animal with a trunk",
        "GIRAFFE" to "Tallest animal with a long neck",
        "PENGUIN" to "Flightless bird that lives in cold regions",
        "DOLPHIN" to "Intelligent marine mammal that jumps",
        "KANGAROO" to "Australian marsupial that hops",
        "RHINOCEROS" to "Large mammal with one or two horns",
        "OCTOPUS" to "Eight-armed sea creature",
        "CHAMELEON" to "Lizard that can change colors"
    )

    private val fruitHints = mapOf(
        "APPLE" to "Red or green fruit that keeps the doctor away",
        "BANANA" to "Yellow fruit that monkeys love",
        "GRAPE" to "Small round fruit that grows in bunches",
        "PEAR" to "Bell-shaped fruit with sweet flesh",
        "KIWI" to "Small brown fruit with green flesh and black seeds",
        "ORANGE" to "Round citrus fruit with a thick rind",
        "PINEAPPLE" to "Tropical fruit with spiky skin",
        "WATERMELON" to "Large green fruit with red flesh and black seeds",
        "STRAWBERRY" to "Red heart-shaped berry with small seeds",
        "BLUEBERRY" to "Small blue-purple berry that grows in clusters",
        "POMEGRANATE" to "Red fruit filled with juicy seeds",
        "PASSIONFRUIT" to "Purple fruit with tart, seedy pulp",
        "DRAGONFRUIT" to "Pink fruit with white flesh and black seeds",
        "BLACKBERRY" to "Dark purple berry that grows on thorny bushes",
        "RASPBERRY" to "Red berry that grows on canes"
    )

    private val countryHints = mapOf(
        "JAPAN" to "Land of the rising sun, known for sushi",
        "CHINA" to "Most populous country with the Great Wall",
        "INDIA" to "Land of spices and the Taj Mahal",
        "EGYPT" to "Ancient civilization with pyramids",
        "ITALY" to "Boot-shaped country known for pizza",
        "AUSTRALIA" to "Island continent with kangaroos",
        "CANADA" to "Northern country with maple leaves",
        "BRAZIL" to "Largest country in South America",
        "GERMANY" to "European country known for beer",
        "FRANCE" to "Country of love and the Eiffel Tower",
        "ARGENTINA" to "South American country known for tango",
        "INDONESIA" to "Archipelago with thousands of islands",
        "MALAYSIA" to "Southeast Asian country with twin towers",
        "THAILAND" to "Land of smiles and beautiful temples",
        "VIETNAM" to "Southeast Asian country shaped like an S"
    )

    private val sportHints = mapOf(
        "SOCCER" to "Most popular sport played with a round ball",
        "TENNIS" to "Racket sport played on a court",
        "GOLF" to "Sport where you hit a ball into a hole",
        "SWIM" to "Moving through water using your body",
        "RUN" to "Moving quickly on foot",
        "BASKETBALL" to "Sport where you shoot a ball through a hoop",
        "VOLLEYBALL" to "Team sport played with a net",
        "BASEBALL" to "Sport with bats, balls, and bases",
        "HOCKEY" to "Fast-paced sport played on ice",
        "CRICKET" to "Popular sport in England and India",
        "BADMINTON" to "Racket sport with a shuttlecock",
        "WRESTLING" to "Combat sport with grappling",
        "GYMNASTICS" to "Sport with flips and balance",
        "FENCING" to "Sword fighting sport",
        "ARCHERY" to "Sport of shooting arrows"
    )

    private val foodHints = mapOf(
        "PIZZA" to "Round flatbread topped with cheese and tomato sauce",
        "PASTA" to "Italian dish made from flour and water",
        "BREAD" to "Baked food made from flour and water",
        "RICE" to "Staple food in many Asian countries",
        "CAKE" to "Sweet baked dessert often served at celebrations",
        "BURGER" to "Sandwich with a cooked patty between buns",
        "SUSHI" to "Japanese dish with vinegared rice and seafood",
        "TACOS" to "Mexican dish with folded tortilla and fillings",
        "NOODLE" to "Long thin strip of pasta or dough",
        "SALAD" to "Dish of mixed raw vegetables",
        "SPAGHETTI" to "Long thin pasta often served with sauce",
        "LASAGNA" to "Layered pasta dish with cheese and sauce",
        "CURRY" to "Spicy dish with sauce from South Asia",
        "STEAK" to "Thick slice of cooked beef",
        "SANDWICH" to "Two slices of bread with filling between"
    )

    private fun getWordsForCategory(category: String): Map<String, List<String>> {
        return when (category) {
            "Animals" -> animalWords
            "Fruits" -> fruitWords
            "Countries" -> countryWords
            "Sports" -> sportWords
            "Food" -> foodWords
            else -> animalWords // Default to animals if category not found
        }
    }

    private fun getHintForWord(word: String): String {
        return when {
            word in animalHints -> animalHints[word]!!
            word in fruitHints -> fruitHints[word]!!
            word in countryHints -> countryHints[word]!!
            word in sportHints -> sportHints[word]!!
            word in foodHints -> foodHints[word]!!
            else -> "Guess the word!"
        }
    }

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
            heartIcon = findViewById(R.id.heart_icon)
            konfettiView = findViewById(R.id.konfettiView)
            hintTextView = findViewById(R.id.hint_text_view)
            val returnHomeButton = findViewById<ImageView>(R.id.return_home)
            Log.d("GameActivity", "Views initialized")

            // Set up home button click listener
            returnHomeButton.setOnClickListener {
                // Save the current level as the last checkpoint
                val category = intent.getStringExtra("category") ?: "Animals"
                val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
                val level = intent.getIntExtra("level", 1)
                
                val prefs = getSharedPreferences("game_progress", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putInt("${category}_${difficulty}_last_level", level)
                editor.apply()

                // Return to difficulty page
                val intent = Intent(this, DifficultyActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
                finish()
            }

            // Get game parameters from intent
            val category = intent.getStringExtra("category") ?: "Animals"
            val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
            val level = intent.getIntExtra("level", 1)
            Log.d("GameActivity", "Category: $category, Difficulty: $difficulty, Level: $level")

            // Update level title
            levelTitleTextView.text = "Level $level"
            levelTitleTextView.setTextColor(Color.parseColor("#5B400D"))
            levelTitleTextView.typeface = ResourcesCompat.getFont(this, R.font.daydream)

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

    private fun setupGame(category: String, difficulty: String, level: Int) {
        try {
            // Get word based on category and difficulty
            val words = getWordsForCategory(category)
            currentWord = words[difficulty]?.get((level - 1) % 5) ?: "CAT"
            Log.d("GameActivity", "Selected word: $currentWord")

            // Set the hint for the current word
            val hint = getHintForWord(currentWord)
            hintTextView.text = hint
            hintTextView.setTextColor(Color.parseColor("#5B400D"))
            hintTextView.typeface = ResourcesCompat.getFont(this, R.font.vhs_gothic)

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
            rowLayoutParams.gravity = Gravity.CENTER
            rowLayoutParams.setMargins(0, 2, 0, 2)

            for (letter in alphabet) {
                if (buttonCount % 7 == 0) {
                    // Create a new row layout
                    currentRow = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = rowLayoutParams
                        gravity = Gravity.CENTER
                    }
                    lettersContainer.addView(currentRow)
                }

                val typeface = ResourcesCompat.getFont(this, R.font.vhs_gothic)

                val button = Button(this).apply {
                    text = letter.toString()
                    textSize = 14f
                    setBackgroundResource(R.drawable.button_background)
                    typeface?.let { this.typeface = it }
                    layoutParams = LinearLayout.LayoutParams(
                        20, // Fixed width
                        80 // Fixed height
                    ).apply {
                        weight = 1f
                        setMargins(2, 2, 2, 2)
                    }
                    setTextColor(Color.parseColor("#ffd493"))
                    gravity = Gravity.CENTER
                    minWidth = 0
                    minHeight = 0
                    setPadding(2, 2, 2, 2)

                    setOnClickListener {
                        handleLetterGuess(letter)
                        isEnabled = false
                        alpha = 0.5f
                    }
                }

                currentRow?.addView(button)
                buttonCount++
            }

            // Add invisible buttons to the last row if needed to maintain equal width
            val remainingButtons = 7 - (buttonCount % 7)
            if (remainingButtons < 7 && remainingButtons > 0) {
                for (i in 0 until remainingButtons) {
                    val invisibleButton = Button(this).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            20, // Fixed width
                            80 // Fixed height
                        ).apply {
                            weight = 1f
                            setMargins(2, 2, 2, 2)
                        }
                        visibility = View.INVISIBLE
                    }
                    currentRow?.addView(invisibleButton)
                }
            }

            Log.d("GameActivity", "Created $buttonCount letter buttons")
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in setupLetterButtons", e)
            Toast.makeText(this, "Error setting up letter buttons", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfetti() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
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
                    showConfetti() // Show confetti when winning
                    Toast.makeText(this, "Level Complete!", Toast.LENGTH_SHORT).show()

                    val nextLevel = intent.getIntExtra("level", 1) + 1
                    val category = intent.getStringExtra("category") ?: "Animals"
                    val difficulty = intent.getStringExtra("difficulty") ?: "Easy"

                    // Save the current level progress
                    val prefs = getSharedPreferences("game_progress", MODE_PRIVATE)
                    val editor = prefs.edit()
                    
                    // Check if this difficulty is already completed
                    val isCompleted = when (difficulty) {
                        "Easy" -> prefs.getBoolean("${category}_easy_completed", false)
                        "Medium" -> prefs.getBoolean("${category}_medium_completed", false)
                        else -> false
                    }

                    // Only update progress if the difficulty is not already completed
                    if (!isCompleted) {
                        val currentLevel = intent.getIntExtra("level", 1)
                        val lastLevel = prefs.getInt("${category}_${difficulty}_last_level", 1)
                        if (currentLevel >= lastLevel) {
                            editor.putInt("${category}_${difficulty}_last_level", nextLevel)
                        }
                    }
                    editor.apply()

                    // Check if all levels are completed
                    if (nextLevel > 5) {
                        // All levels completed, redirect to difficulty page
                        Toast.makeText(this, "Congratulations! You've completed $difficulty difficulty!", Toast.LENGTH_LONG).show()
                        
                        // Save completion status
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
                remainingGuesses--
                updateHangmanImage()
                if (remainingGuesses <= 0) {
                    // Game over, show the retry fragment
                    val category = intent.getStringExtra("category") ?: "Animals"
                    val difficulty = intent.getStringExtra("difficulty") ?: "Easy"
                    val level = intent.getIntExtra("level", 1)

                    // Set the loser background
                    findViewById<FrameLayout>(R.id.root_layout).setBackgroundResource(R.drawable.loserbackground)

                    // Change level title color to white
                    levelTitleTextView.setTextColor(Color.parseColor("#D52E53"));

                    // Show RetryLevelFragment
                    Toast.makeText(this, "Game Over! The word was: $currentWord", Toast.LENGTH_SHORT).show()

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
                cornerRadius = 10f
            }
            
            // Adjust box width based on word length
            val fixedBoxWidth = when (currentWord.length) {
                in 1..5 -> 80  // Smaller boxes for short words
                in 6..8 -> 60  // Medium boxes for medium words
                else -> 50     // Smaller boxes for longer words
            }
            
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
                                top - 10,
                                x.toInt() + fixedBoxWidth,
                                bottom + 10
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
            wordTextView.typeface = ResourcesCompat.getFont(this, R.font.vhs_gothic)
            wordTextView.textSize = when (currentWord.length) {
                in 1..5 -> 24f  // Larger text for short words
                in 6..8 -> 20f  // Medium text for medium words
                else -> 16f     // Smaller text for longer words
            }
            
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
            
            // Update heart icon alpha based on remaining guesses
            val heartAlpha = when (remainingGuesses) {
                6 -> 1.0f
                5 -> 0.8f
                4 -> 0.6f
                3 -> 0.4f
                2 -> 0.2f
                1 -> 0.1f
                else -> 0.0f
            }
            heartIcon.alpha = heartAlpha
        } catch (e: Exception) {
            Log.e("GameActivity", "Error in updateHangmanImage", e)
        }
    }
}