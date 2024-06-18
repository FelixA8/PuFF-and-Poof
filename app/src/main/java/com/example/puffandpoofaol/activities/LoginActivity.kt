package com.example.puffandpoofaol.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USERNAME_KEY = "username_key"
        const val PASSWORD_KEY = "password_key"
    }
    private lateinit var db: DatabaseHelper
    private lateinit var sharedpreferences: SharedPreferences
    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = DatabaseHelper(this)
        //get shared Preferences Data to keep data whether the user is logged or not.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(USERNAME_KEY, null)
        password = sharedpreferences.getString(PASSWORD_KEY, null)
        //If the username or password is empty in the shared preferences, do not proceed to the display activity.
        if(username != null && password != null) {
            println(username)
            println(password)
            val intent = Intent(this, DisplayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        //Login button, when pressed it will validate and if true, proceed to display activity.
        binding.btnToOTPPage.setOnClickListener {
            val loggedUser = db.validateUserLogin(binding.etLoginUsername.text.toString(), binding.etLoginPassword.text.toString())
            if(loggedUser !== null) {
                val intent = Intent(this, OTPActivity::class.java).apply {
                    putExtra("userid", loggedUser.userId)
                    putExtra("telephonenumber", loggedUser.userTelephoneNumber)
                    val editor = sharedpreferences.edit()
                    editor.clear()
                    editor.putString(USERNAME_KEY, loggedUser.userName)
                    editor.putString(PASSWORD_KEY, loggedUser.userPassword)
                    editor.apply()
                }
                Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login Failed, Please check your Username and Password.", Toast.LENGTH_LONG).show()
                println("false")
            }
        }
        //Go to register page.
        binding.tvToRegisterPage.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent);
        }
    }
}