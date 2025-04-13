package cit.edu.letterlasso

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class LogoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        val btnLogout = findViewById<Button>(R.id.button_logout)
        val btnCancel = findViewById<Button>(R.id.button_cancel)

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }

        btnCancel.setOnClickListener {
            finish()
        }

    }
}
