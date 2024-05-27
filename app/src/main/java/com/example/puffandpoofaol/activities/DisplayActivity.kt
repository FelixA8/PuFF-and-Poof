package com.example.puffandpoofaol.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.MessageQueue
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.adapters.DollAdapter
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.Doll
import com.example.puffandpoofaol.data.Global
import com.example.puffandpoofaol.data.User
import com.example.puffandpoofaol.databinding.ActivityDisplayBinding
import com.example.puffandpoofaol.fragments.HistoryFragment
import com.example.puffandpoofaol.fragments.HomeFragment
import com.example.puffandpoofaol.fragments.ProfileFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DisplayActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USERNAME_KEY = "username_key"
        const val PASSWORD_KEY = "password_key"
    }
    private lateinit var db: DatabaseHelper
    private lateinit var sharedpreferences: SharedPreferences
    private var password: String? = null
    private var username: String? = null
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialFragment = intent.extras?.getString("fragment")
        val bottomNav = binding.bottomNav

        //Get logged user data
        sharedpreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(LoginActivity.USERNAME_KEY, null)
        password = sharedpreferences.getString(LoginActivity.PASSWORD_KEY, null)
        db = DatabaseHelper(this)
        currentUser = db.validateUserLogin(username!!, password!!)
        Global.currentUser = currentUser!!
        if(currentUser == null) {
            //ERROR MESSAGE
            println("error")
        }

        //set the initial fragment.
        if(initialFragment == "history") {
            renderFragment(HistoryFragment())
            val view:View = bottomNav.findViewById(R.id.history)
            view.performClick()
        } else {
            renderFragment(HomeFragment())
        }

        //Set the bottom navbar and its functionality.
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    renderFragment(HomeFragment())
                    true
                }
                R.id.history -> {
                    renderFragment(HistoryFragment())
                    true
                }
                R.id.profile -> {
                    renderFragment(ProfileFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    //Render the fragment
    private  fun renderFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}