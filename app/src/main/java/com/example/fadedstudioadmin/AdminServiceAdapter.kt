package com.example.fadedstudioadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdminServiceAdapter(
    private val services: List<Service>,
    private val onServiceClick: (Service) -> Unit
) : RecyclerView.Adapter<AdminServiceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val ivServiceImage: ImageView = view.findViewById(R.id.ivServiceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = services[position]
        holder.tvName.text = service.name
        holder.tvDetail.text = service.detail
        holder.tvPrice.text = "₱${service.price}"

        // Load image using Glide
        if (service.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(service.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivServiceImage)
        } else {
            holder.ivServiceImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // When you click a service in the list, it triggers the edit screen!
        holder.itemView.setOnClickListener {
            onServiceClick(service)
        }
    }

    override fun getItemCount() = services.size
}