package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import cit.edu.letterlasso.util.toast
import cit.edu.letterlasso.util.txt

class RegisterActivity : Activity() {

    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edittext_username = findViewById<EditText>(R.id.email)
        val edittext_password = findViewById<EditText>(R.id.password)

        spinnerDay = findViewById(R.id.spinner_day)
        spinnerMonth = findViewById(R.id.spinner_month)
        spinnerYear = findViewById(R.id.spinner_year)

        val register_button = findViewById<TextView>(R.id.register)

        // Populate Day, Month, and Year Spinners
        populateSpinners()

        register_button.setOnClickListener {
            val username = edittext_username.txt()
            val password = edittext_password.txt()

            if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                this.toast("Username and Password cannot be empty")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                this.toast("Please enter a valid email address")
                return@setOnClickListener
            }

            val selectedDay = spinnerDay.selectedItem.toString()
            val selectedMonth = spinnerMonth.selectedItem.toString()
            val selectedYear = spinnerYear.selectedItem.toString()

            if (selectedDay == "Day" || selectedMonth == "Month" || selectedYear == "Year") {
                this.toast("Please select a valid birthdate")
                return@setOnClickListener
            }

            val birthDate = "$selectedMonth $selectedDay, $selectedYear"
            this.toast("Registered successfully!")

            // Go to Login Activity after successful registration
            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    putExtra("username", username)
                    putExtra("password", password)
                }
            )
        }

        val button_login = findViewById<TextView>(R.id.account_already)
        button_login.setOnClickListener {
            Log.e("LetterLasso", "Already have an account now is clicked!")
            this.toast("Already have an account now is clicked!")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val close_button = findViewById<Button>(R.id.register_close_button)
        close_button.setOnClickListener {
            Log.e("LetterLasso", "close button is now clicked!")
            this.toast("Heading to login page!")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populateSpinners() {
        val days = (1..31).map { it.toString() }.toMutableList().apply { add(0, "Day") }
        val months = listOf(
            "Month", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val years = (2025 downTo 1950).map { it.toString() }.toMutableList().apply { add(0, "Year") }

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)

        spinnerDay.adapter = dayAdapter
        spinnerMonth.adapter = monthAdapter
        spinnerYear.adapter = yearAdapter
    }
}
