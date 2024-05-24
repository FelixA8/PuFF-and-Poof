package com.example.puffandpoofaol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.adapters.DollAdapter
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.Doll
import com.example.puffandpoofaol.databinding.FragmentHomeBinding
import org.json.JSONException
import org.json.JSONObject

//WARNING:
//Image could not load because:
//1. Poor Internet Access
//2. ImageURL has expired (404 not found)

class HomeFragment() : Fragment() {
    private lateinit var db: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.homeRV.isNestedScrollingEnabled = false
        db = DatabaseHelper(requireContext())
        var dollList = db.selectAllDoll()
        println(dollList)
        if(dollList.isEmpty()) {
            try {
                val queue = Volley.newRequestQueue(context)
                val request = JsonObjectRequest(
                    Request.Method.GET, "https://api.npoint.io/9d7f4f02be5d5631a664", null, {
                            response -> try {
                        dollList = parseJSON(response)
                        val adapter = DollAdapter(requireContext(), listDolls = dollList)
                        binding.homeRV.adapter = adapter
                        binding.homeRV.layoutManager = GridLayoutManager(requireContext(), 2)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    },
                    {
                            error -> println(error)
                    }
                )
                queue.add(request)
            } catch (e: Exception) {
                println(e)
            }
        } else{
            val adapter = DollAdapter(requireContext(), listDolls = dollList)
            binding.homeRV.adapter = adapter
            binding.homeRV.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        return binding.root
    }
    private fun parseJSON(jsonObject: JSONObject): ArrayList<Doll> {
        val dollList : ArrayList<Doll> = ArrayList()
        try {
            val dollArray = jsonObject.getJSONArray("dolls")
            for(i in 0 until dollArray.length()) {
                val dollObject = dollArray.getJSONObject(i)
                val dollName = dollObject.getString("name")
                val dollDesc = dollObject.getString("desc")
                val dollRating = dollObject.getString("rating").toDouble()
                val dollSize = dollObject.getString("size")
                val dollPrice = dollObject.getString("price").toInt()
                val dollImageURL = dollObject.getString("imageLink")
                db.insertDoll(Doll(i, dollName,dollDesc, dollSize, dollPrice, dollRating, dollImageURL))
                dollList.add(Doll(i, dollName,dollDesc, dollSize, dollPrice, dollRating, dollImageURL))
            }
        } catch (e: java.lang.Exception) {
            println(e)
        }
        return dollList
    }
}