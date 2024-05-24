package com.example.puffandpoofaol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.puffandpoofaol.activities.ClosingActivity
import com.example.puffandpoofaol.activities.DisplayActivity
import com.example.puffandpoofaol.activities.LoginActivity

//ROOT ACTIVITY
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //CHANGE PAGE DIRECTLY TO THE LOGIN PAGE
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}