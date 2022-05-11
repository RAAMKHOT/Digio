package com.ramagouda.screens.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ramagouda.R
import com.ramagouda.helper.CommonUtils
import com.squareup.picasso.Picasso
import java.io.File

class PdfListAdapter(
    private var cropList: ArrayList<String>,
    private var context: Context,
    private var pdfListView: PdfListView
) :
    RecyclerView.Adapter<PdfListAdapter.CropViewHolder>() {
    private var tempList: ArrayList<String>? = null

    inner class CropViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        if (cropItem != null) {
            val file = File(cropItem)
            holder.textViewTitle.text = file.name
            holder.textViewTitle.setTextColor(context.getColor(R.color.titleColor))
        } else {
            holder.textViewTitle.text = context.getString(R.string.no_title)
            holder.textViewTitle.setTextColor(context.getColor(R.color.titleHintColor))
        }
        if (cropItem != null) {
            holder.textViewDescription.text = "Path : \n$cropItem"
            holder.textViewDescription.setTextColor(context.getColor(R.color.descriptionColor))
        } else {
            holder.textViewDescription.text = context.getString(R.string.no_dscp)
            holder.textViewDescription.setTextColor(context.getColor(R.color.titleHintColor))
        }

        holder.itemView.setOnClickListener {
            val file = File(cropItem)
            pdfListView.sendFileToServer(file)
            CommonUtils.showToast(context,"Upload Started!!")
            Picasso.get()
                .load(R.drawable.pdf_upload)
                .placeholder(R.drawable.pdf_upload)
                .into(holder.imageViewFact)
        }
        Picasso.get()
            .load(R.drawable.pdf_down)
            .placeholder(R.drawable.pdf_down)
            .into(holder.imageViewFact)
    }

    override fun getItemCount(): Int {
        return cropList.size
    }

    fun setList(listOfFarmers: ArrayList<String>) {
        tempList = ArrayList()
        cropList.addAll(listOfFarmers)
        tempList!!.addAll(listOfFarmers)
        notifyDataSetChanged()
    }
}