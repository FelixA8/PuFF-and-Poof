package com.example.puffandpoofaol.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.puffandpoofaol.R
import com.example.puffandpoofaol.data.DatabaseHelper
import com.example.puffandpoofaol.data.Global
import com.example.puffandpoofaol.databinding.ActivityDollDetailBinding
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DollDetailActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //GET The data of the doll from binding/ previous activity.
        val binding = ActivityDollDetailBinding.inflate(layoutInflater)
        val id = intent.extras?.getString("dollId")
        val title = intent.extras?.getString("dollName")
        val imageUrl = intent.extras?.getString("dollImage")
        val price = intent.extras?.getString("dollPrice")
        val rating = intent.extras?.getString("dollRating")
        val size = intent.extras?.getString("dollSize")
        val description = intent.extras?.getString("dollDetail")
        db = DatabaseHelper(this)
        binding.tvDollDetailName.text = title
        Picasso.get().load(imageUrl).into(binding.ivDollDetailImage)
        binding.tvDollDetailPrice.text = price
        binding.tvDetailRating.text = rating
        binding.tvDetailSize.text = size
        binding.tvProductDetail.text = description
        binding.ivDetailBack.setOnClickListener {
            finish()
        }

        //Button Go to history page.
        binding.ivDetailGoToHistory.setOnClickListener {
            val intent = Intent(this, DisplayActivity::class.java).apply {
                putExtra("fragment", "history")
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //btn Buy Product.
        binding.btnBuyProduct.setOnClickListener {
            //Check if amount is 0 or less, then set error.
            if (binding.etDollDetailAmount.text.isEmpty() || binding.etDollDetailAmount.text.toString().toInt() <= 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Oops! Please Input a valid amount which is 1 or more.")
                builder.setPositiveButton("OK") { dialog, which ->
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                //Get the current time stamp in yyyy-mm-dd hh:mm:ss
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = current.format(formatter)
                //insert new transaction
                db.insertTransaction(Global.currentUser.userId, id!!.toInt(), binding.etDollDetailAmount.text.toString().toInt(), formatted, title.toString())
                Toast.makeText(this, "Purchase Success!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        setContentView(binding.root)
    }
}