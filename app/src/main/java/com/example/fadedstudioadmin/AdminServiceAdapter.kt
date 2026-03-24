package com.example.fadedstudioadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminServiceAdapter(private val services: List<Service>) : RecyclerView.Adapter<AdminServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvAdminServiceName)
        val price: TextView = view.findViewById(R.id.tvAdminServicePrice)
        val btnEdit: Button = view.findViewById(R.id.btnEditRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.name.text = service.serviceName
        holder.price.text = "₱${service.price}"

        holder.btnEdit.setOnClickListener {
            // Logic to open AdminAddServiceFragment in "Edit Mode"
        }
    }

    override fun getItemCount() = services.size
}