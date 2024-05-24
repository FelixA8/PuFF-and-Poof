package com.example.puffandpoofaol.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.puffandpoofaol.activities.DollDetailActivity
import com.example.puffandpoofaol.data.Doll
import com.example.puffandpoofaol.databinding.DollCardBinding
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.URI
import java.net.URL

class DollAdapter(private val context:Context, val listDolls: ArrayList<Doll>):RecyclerView.Adapter<DollAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemBinding: DollCardBinding):RecyclerView.ViewHolder(itemBinding.root){
        private val dollImage = itemBinding.ivDollCardImage
        val dollTitle = itemBinding.tvDollCardTitle
        val dollPrice = itemBinding.tvDollCardPrice
        val dollSize = itemBinding.tvDollCardSize
        val dollRating = itemBinding.tvDollCardRating

        fun bind(imageUrl: String) {
            Picasso.get().load(imageUrl).into(dollImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainViewHolder {
        return MainViewHolder(DollCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listDolls.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val doll = listDolls[position]
        holder.bind(doll.dollImageURL)
        holder.dollPrice.text = doll.dollPrice.toString()
        holder.dollRating.text = doll.dollRating.toString()
        holder.dollTitle.text = doll.dollName
        holder.dollSize.text = doll.dollSize
        holder.itemView.setOnClickListener {
            println(doll)
            val intent = Intent(context, DollDetailActivity::class.java).apply {
                putExtra("dollName", doll.dollName)
                putExtra("dollDetail", doll.dollDesc)
                putExtra("dollImage", doll.dollImageURL)
                putExtra("dollPrice", doll.dollPrice.toString())
                putExtra("dollRating", doll.dollRating.toString())
                putExtra("dollSize", doll.dollSize)
                putExtra("dollId", doll.id.toString())
            }
            startActivity(context, intent, Bundle.EMPTY)
        }
    }
}