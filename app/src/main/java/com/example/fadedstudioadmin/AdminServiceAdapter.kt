package com.example.fadedstudioadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Added a click listener to the constructor!
class AdminServiceAdapter(
    private var services: List<Service>,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<AdminServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvServiceName)
        val tvPrice: TextView = view.findViewById(R.id.tvServicePrice)
        val tvDuration: TextView = view.findViewById(R.id.tvServiceDuration)
        val ivImage: ImageView = view.findViewById(R.id.ivServiceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]

        holder.tvName.text = service.serviceName.ifEmpty { "Unnamed Service" }

        val priceText = service.price
        holder.tvPrice.text = if (priceText.startsWith("₱") || priceText.startsWith("P")) priceText else "₱$priceText"

        holder.tvDuration.text = service.duration.ifEmpty { "No duration set" }

        if (service.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(service.imageUrl)
                .into(holder.ivImage)
        } else {
            holder.ivImage.setImageDrawable(null)
        }

        // MAGIC: When the whole card is clicked, pass the service data back to the fragment
        holder.itemView.setOnClickListener {
            onItemClick(service)
        }
    }

    override fun getItemCount() = services.size

    fun updateList(newList: List<Service>) {
        services = newList
        notifyDataSetChanged()
    }
}