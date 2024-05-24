package com.example.puffandpoofaol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.adapters.DollAdapter
import com.example.puffandpoofaol.adapters.TransactionAdapter
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.Global
import com.example.puffandpoofaol.databinding.FragmentHistoryBinding
import com.example.puffandpoofaol.databinding.FragmentHomeBinding

class HistoryFragment : Fragment() {
    private lateinit var db: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHistoryBinding.inflate(layoutInflater)
        db = DatabaseHelper(requireContext())
        var transactionList = db.selectTransaction(Global.currentUser.userId)
        if(transactionList.isEmpty()) {
            println("empty")
        } else {
            val adapter = TransactionAdapter(requireContext(), listTransaction = transactionList)
            println("transactionList = $transactionList")
            binding.transactionRV.adapter = adapter
            binding.transactionRV.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}