package com.example.puffandpoofaol.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.puffandpoofaol.databinding.ActivityLoginBinding
import com.example.puffandpoofaol.databinding.ActivityOtpBinding
import kotlin.random.Random

class OTPActivity : AppCompatActivity() {

    private lateinit var smsManager: SmsManager

    private lateinit var phoneNumber:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomNumber = Random.nextInt(1000, 10000)
        val message = "DO NOT SHARE! Your OTP Number is ${randomNumber}"
        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            phoneNumber = bundle.getString("telephonenumber")!!
        }
        smsManager = SmsManager.getDefault()
        binding.tvOTPInform.text = "An OTP number has been sent to " + phoneNumber + ". Please check your messages."
        checkSendSMSPermission(phoneNumber, message)

        binding.tvResendOTP.setOnClickListener {
            checkSendSMSPermission(phoneNumber, message)
            Toast.makeText(this, "OTP has ben resent! Please check your messages again!", Toast.LENGTH_LONG).show()
        }
        //SMS Button.
        binding.btnToDisplayPage.setOnClickListener {
            if(binding.etOTPCode.text.length < 4) {
                Toast.makeText(this, "Please input a valid OTP Code", Toast.LENGTH_SHORT).show()
            } else if (binding.etOTPCode.text.toString() == randomNumber.toString()) {
                val i = Intent(this, DisplayActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                Toast.makeText(this, "Success! Welcome to Puff and Poof", Toast.LENGTH_SHORT).show()
            } else {
                println(randomNumber)
                Toast.makeText(this, "Wrong OTP Code. Please enter again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkSendSMSPermission(phoneNumber: String, message:String){
        //Check if permission is enabled.
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            //Request permission.
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100);
        } else {
            sendSMS(phoneNumber, message)
        }
    }
    //Send SMS to phone
    fun sendSMS(phoneNumber: String, message: String) {
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}