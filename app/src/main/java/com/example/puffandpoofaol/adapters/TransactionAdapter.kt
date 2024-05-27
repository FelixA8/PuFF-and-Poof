package com.example.puffandpoofaol.adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.activities.DollDetailActivity
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.Doll
import com.example.puffandpoofaol.data.Transaction
import com.example.puffandpoofaol.databinding.DollCardBinding
import com.example.puffandpoofaol.databinding.TransactionCardBinding
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.URI
import java.net.URL

class TransactionAdapter(private val context:Context, private var listTransaction: ArrayList<Transaction>):RecyclerView.Adapter<TransactionAdapter.MainViewHolder>() {
    private lateinit var db: DatabaseHelper

    inner class MainViewHolder(private val itemBinding: TransactionCardBinding):RecyclerView.ViewHolder(itemBinding.root){
        val transactionId = itemBinding.tvHistoryTransactionID
        val dollName = itemBinding.tvHistoryDollName
        val transactionAmount = itemBinding.tvHistoryTransactionAmount
        val transactionDate = itemBinding.tvHistoryTransactionDate
        val deleteBtn = itemBinding.btnHistoryDelete
        val updateBtn = itemBinding.btnHistoryUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainViewHolder {
        return MainViewHolder(TransactionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int {
        return listTransaction.size
    }

    //Description: Binds the data from the Doll object at the specified position to the UI components in the MainViewHolder.
    // Sets up an OnClickListener to handle item clicks, which responsible for updating/ deleting items.
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        db = DatabaseHelper(context)
        val transaction = listTransaction[position]

        holder.transactionDate.text = transaction.transactionDate
        holder.transactionId.text = transaction.transactionID.toString()
        holder.dollName.text = transaction.dollName
        holder.transactionAmount.text = "x${transaction.transactionAmount}"
        //Delete Item Button
        holder.deleteBtn.setOnClickListener {
            db.deleteTransaction(transactionID = transaction.transactionID)
            listTransaction.removeAt(position) //refresh the current data by removing the data from previous arraylist.
            updateData(listTransaction)
        }
        holder.updateBtn.setOnClickListener {
            val dialog = CustomDialog(context, transaction.transactionAmount) {
                amount ->
                run {
                    val newTransaction = Transaction(
                        transaction.transactionID,
                        transaction.userID,
                        transaction.dollID,
                        amount.toInt(),
                        transaction.transactionDate,
                        transaction.dollName
                    )
                   db.updateTransaction(newTransaction)
                   listTransaction[position].transactionAmount = amount.toInt()
                    notifyDataSetChanged()
                }
            }
            dialog.show()
        }
    }

    fun updateData(newDataList: ArrayList<Transaction>) {
        listTransaction = newDataList //refresh the new transaction.
        notifyDataSetChanged()
    }
}

class CustomDialog(context: Context, private val initialValue:Int, private val onConfirmClickListener: (String) -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        val amountEditText = findViewById<EditText>(R.id.etCustomDialogAmount)
        val updateButton = findViewById<Button>(R.id.btnCustomDialogUpdate)
        val cancelButton = findViewById<Button>(R.id.btnCustomDialogCancel)
        amountEditText.setText(initialValue.toString())

        updateButton.setOnClickListener {
            val amount = amountEditText.text.toString()

            if(amount.isEmpty()) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage("Oops! Please Input a valid amount which is 1 or more.")
                builder.setPositiveButton("OK") { dialog, which ->
                }
                val dialog = builder.create()
                dialog.show()
            } else if(amount.toInt() <= 0) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                builder.setTitle("Error")
                builder.setMessage("Oops! Please Input a valid amount which is 1 or more.")
                builder.setPositiveButton("OK") { dialog, which ->
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                onConfirmClickListener.invoke(amount)
                dismiss()
            }

        }

        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}