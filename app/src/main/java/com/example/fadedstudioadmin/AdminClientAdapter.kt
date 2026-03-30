package com.example.fadedstudioadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminClientAdapter(private var clients: List<User>) :
    RecyclerView.Adapter<AdminClientAdapter.ClientViewHolder>() {

    class ClientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvClientName)
        val tvEmail: TextView = view.findViewById(R.id.tvClientEmail)
        val tvPhone: TextView = view.findViewById(R.id.tvClientPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_client, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.tvName.text = client.name
        holder.tvEmail.text = client.email
        holder.tvPhone.text = client.phone
    }

    override fun getItemCount() = clients.size

    fun updateList(newList: List<User>) {
        clients = newList
        notifyDataSetChanged()
    }
}