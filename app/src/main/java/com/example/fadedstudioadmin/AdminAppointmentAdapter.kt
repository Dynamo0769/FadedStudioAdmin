package com.example.fadedstudioadmin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAppointmentAdapter(
    private var appointments: List<Appointment>,
    private val onActionClick: (Appointment, String) -> Unit
) : RecyclerView.Adapter<AdminAppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvServiceName: TextView = view.findViewById(R.id.tvService)
        val tvUserEmail: TextView = view.findViewById(R.id.tvRef)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val layoutButtons: LinearLayout = view.findViewById(R.id.layoutButtons)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appointments[position]

        holder.tvServiceName.text = item.serviceName ?: "Unknown Service"
        holder.tvUserEmail.text = item.userEmail ?: "No Email"

        val currentStatus = item.status?.uppercase() ?: "PENDING"
        holder.tvStatus.text = "STATUS: $currentStatus"

        when (currentStatus) {
            "ACCEPTED" -> holder.tvStatus.setTextColor(Color.GREEN)
            "REJECTED" -> holder.tvStatus.setTextColor(Color.RED)
            else -> holder.tvStatus.setTextColor(Color.parseColor("#FFD700"))
        }

        if (item.isUpdating) {
            holder.progressBar.visibility = View.VISIBLE
            holder.layoutButtons.visibility = View.GONE
        } else {
            holder.progressBar.visibility = View.GONE
            holder.layoutButtons.visibility = if (currentStatus == "PENDING") View.VISIBLE else View.GONE
        }

        holder.btnAccept.setOnClickListener { onActionClick(item, "ACCEPTED") }
        holder.btnReject.setOnClickListener { onActionClick(item, "REJECTED") }
    }

    override fun getItemCount() = appointments.size
}