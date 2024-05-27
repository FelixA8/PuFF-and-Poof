package com.example.puffandpoofaol.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.activities.ClosingActivity
import com.example.puffandpoofaol.activities.LoginActivity
import com.example.puffandpoofaol.data.Global.currentUser
import com.example.puffandpoofaol.data.User
import com.example.puffandpoofaol.databinding.ActivityLoginBinding
import com.example.puffandpoofaol.databinding.FragmentHomeBinding
import com.example.puffandpoofaol.databinding.FragmentProfileBinding

class ProfileFragment(
) : Fragment() {
    private lateinit var sharedpreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(layoutInflater)
        //Get the data with the sharedprefs
        sharedpreferences = this.requireActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        binding.tvProfileUsername.text = currentUser.userName
        binding.tvProfileEmail.text = currentUser.userEmail
        binding.tvProfileGender.text = currentUser.userGender
        binding.tvProfilePhoneNumber.text = currentUser.userTelephoneNumber
        binding.btnLogout.setOnClickListener {
            val intent = Intent(context, ClosingActivity::class.java);
            val editor = sharedpreferences.edit() //edit the sharedprefs
            editor.clear() //clear the data of the sharedprefs
            editor.commit()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return binding.root
    }
}