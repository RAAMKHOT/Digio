package com.telstra.screens.landing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.telstra.R
import com.telstra.models.Rows

class FactsListAdapter(private var cropList: ArrayList<Rows>, private var context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FactsListAdapter.CropViewHolder>() {
    private var tempList: ArrayList<Rows>? = null

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
            holder.textViewTitle.setTextColor(context.getColor(R.color.titleColor))
        } else {
            holder.textViewTitle.text = context.getString(R.string.no_title)
            holder.textViewTitle.setTextColor(context.getColor(R.color.titleHintColor))
        }
        if (cropItem.description != null) {
            holder.textViewDescription.text = cropItem.description
            holder.textViewDescription.setTextColor(context.getColor(R.color.descriptionColor))
        } else {
            holder.textViewDescription.text = context.getString(R.string.no_dscp)
            holder.textViewDescription.setTextColor(context.getColor(R.color.titleHintColor))
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