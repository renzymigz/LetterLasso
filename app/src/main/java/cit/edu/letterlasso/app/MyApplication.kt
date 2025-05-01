package cit.edu.letterlasso.app

import android.app.Application
import android.util.Log

class MyApplication: Application(){

    var username: String?= null
    var email: String? = null
    var password: String? = null
    var birthdate: String? = null

    override fun onCreate() {
        super.onCreate()
        Log.e("Tutorial", "My application is called")
    }

}
