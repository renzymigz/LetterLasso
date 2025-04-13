package cit.edu.letterlasso.util

import android.app.Activity
import android.widget.EditText
import android.widget.Toast

fun EditText.txt(): String {
    return this.text.toString()
}

fun EditText.isValidEntry(): Boolean {
    return this.text.toString().isNullOrEmpty()
}

fun Activity.toast(msg:String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}