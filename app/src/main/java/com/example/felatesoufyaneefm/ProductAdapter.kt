package com.example.felatesoufyaneefm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private var pcList: MutableList<Product>,
    private val itemClickListener: (Product) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.PcViewHolder>() {

    private var selectedPc: Product? = null

    fun submitList(newList: List<Product>) {
        pcList = newList.toMutableList()
        notifyDataSetChanged()
    }

    inner class PcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView? = itemView.findViewById(R.id.tvname)
        val priceTextView: TextView? = itemView.findViewById(R.id.tvprice)
        val imageImageView: ImageView? = itemView.findViewById(R.id.tvimage)
        val searchButton: Button? = itemView.findViewById(R.id.search)

        init {
            itemView.setOnClickListener {
                nameTextView?.text?.toString()?.let { name ->
                    val pc = pcList.find { it.name == name }
                    pc?.let { itemClickListener.invoke(it) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PcViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return PcViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PcViewHolder, position: Int) {
        val currentItem = pcList[position]
        holder.nameTextView?.text = currentItem.name
        holder.priceTextView?.text = currentItem.price.toString()

        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .into(holder.imageImageView!!)
    }

    override fun getItemCount(): Int {
        return pcList.size
    }
}
