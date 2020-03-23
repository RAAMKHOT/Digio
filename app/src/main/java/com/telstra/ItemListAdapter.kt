package com.telstra

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.telstra.models.Rows


class ItemListAdapter(private var cropList: ArrayList<Rows>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ItemListAdapter.CropViewHolder>() {
    private var tempList: ArrayList<Rows>? = null
    private var cropSelectedCount = 0

    inner class CropViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        var textViewDescription: TextView = view.findViewById(R.id.textViewDescription)
        var imageViewFact: ImageView = view.findViewById(R.id.imageViewFact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.facts_item_row, parent, false)

        return CropViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val cropItem = cropList[position]
        if (cropItem.title != null) {
            holder.textViewTitle.text = cropItem.title
            holder.textViewTitle.setTextColor(Color.parseColor("#1f3774"))
        } else {
            holder.textViewTitle.text = "<No title>"
            holder.textViewTitle.setTextColor(Color.parseColor("#c3c3c3"))
        }
        if (cropItem.description != null) {
            holder.textViewDescription.text = cropItem.description
            holder.textViewDescription.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.textViewDescription.text = "<No description>"
            holder.textViewDescription.setTextColor(Color.parseColor("#c3c3c3"))
        }

        Picasso.get()
            .load(cropItem.imageHref)
            .placeholder(R.drawable.placeholder)
            .into(holder.imageViewFact)
    }

    override fun getItemCount(): Int {
        return cropList.size
    }

    fun setList(listOfFarmers: ArrayList<Rows>) {
        tempList = ArrayList()
        cropList.addAll(listOfFarmers)
        tempList!!.addAll(listOfFarmers)
        notifyDataSetChanged()
    }
}