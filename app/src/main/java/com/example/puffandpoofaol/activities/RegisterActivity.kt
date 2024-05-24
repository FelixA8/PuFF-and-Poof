package com.example.puffandpoofaol.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.User
import com.example.puffandpoofaol.databinding.ActivityLoginBinding
import com.example.puffandpoofaol.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etName : EditText
    private lateinit var etPhoneNumber : EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale : RadioButton
    private lateinit var rbFemale : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)



        db = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            binding.rbMale.isChecked = true
            etEmail = binding.etRegisterEmail
            etPassword = binding.etRegisterPassword
            etName = binding.etRegisterUsername
            etPhoneNumber = binding.etRegisterPhoneNumber
            rgGender = binding.rgGender
            rbMale = binding.rbMale
            rbFemale = binding.rbFemale

            if(validate()) {
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }



        binding.tvToLoginPage.setOnClickListener {
            finish();
        }
    }

    private fun validate():Boolean{
        if(etEmail.text.isEmpty() || etPassword.text.isEmpty() || etName.text.isEmpty() || etPhoneNumber.text.isEmpty() ||
            rgGender.checkedRadioButtonId ==-1 ) {
            Toast.makeText(this, "Please Fill out all required forms", Toast.LENGTH_LONG).show()
        } else if(etName.length() < 8) {
            Toast.makeText(this, "Username must have a minimum of 8 characters", Toast.LENGTH_LONG).show()
        } else if(!isEmailValid(etEmail.text.toString())) {
            Toast.makeText(this, "Please Enter a valid email address", Toast.LENGTH_LONG).show()
        } else if(etPassword.text.length < 8) {
            Toast.makeText(this, "Password must have a minimum of 8 characters", Toast.LENGTH_LONG).show()
        } else if(isPhoneValid(etPhoneNumber.text.toString())) {
            Toast.makeText(this, "Please Enter a valid phone number", Toast.LENGTH_LONG).show()
        } else
            if(!db.validateRegisterUsername(etName.text.toString())) {
            Toast.makeText(this, "Username Taken, Please user another username", Toast.LENGTH_LONG).show()
        }else {
            val selectedId: Int = rgGender.checkedRadioButtonId
            val selectedRadioButton : RadioButton = findViewById(selectedId)
            val user = User(-1, etName.text.toString(), etEmail.text.toString(), etPassword.text.toString(), etPhoneNumber.text.toString(), selectedRadioButton.text.toString())
            db.insertUser(user);
            Toast.makeText(this, "Success! Please Login using the registered email!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
    // Function to validate an email address
    private fun isEmailValid(email: String): Boolean {
        return email.contains("@puff.com")
    }
    // Function to validate an phonenumber
    private fun isPhoneValid(phone: String): Boolean {
        return phone.length !in 13 downTo 11
    }
}