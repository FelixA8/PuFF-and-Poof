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
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(USERNAME_KEY, null)
        password = sharedpreferences.getString(PASSWORD_KEY, null)

        if(username != null && password != null) {
            println(username)
            println(password)
            val intent = Intent(this, DisplayActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

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
                startActivity(intent)
            } else {
                println("false")
            }
        }

        binding.tvToRegisterPage.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent);
        }
    }
}