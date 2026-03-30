package com.example.fadedstudioadmin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAppointmentAdapter(
    private var appointments: List<Appointment>,
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit,
    private val onCompleteClick: (Appointment) -> Unit // New Callback
) : RecyclerView.Adapter<AdminAppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvService: TextView = view.findViewById(R.id.tvAptService)
        val tvDateTime: TextView = view.findViewById(R.id.tvAptDateTime)
        val tvStatus: TextView = view.findViewById(R.id.tvAptStatus)

        val llActionButtons: LinearLayout = view.findViewById(R.id.llActionButtons)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val btnComplete: Button = view.findViewById(R.id.btnComplete) // New Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val apt = appointments[position]

        holder.tvName.text = "User ID: ${apt.userId.take(8)}..."
        holder.tvService.text = "Service: ${apt.serviceName}"
        holder.tvDateTime.text = apt.dateTime

        val isPending = apt.status.equals("To be confirm", true) || apt.status.equals("Pending", true)
        val isAccepted = apt.status.equals("Accepted", true)

        val displayStatus = if (isPending) "PENDING" else apt.status.uppercase()
        holder.tvStatus.text = displayStatus

        // MAGIC: Dynamic Button Visibility
        holder.llActionButtons.visibility = if (isPending) View.VISIBLE else View.GONE
        holder.btnComplete.visibility = if (isAccepted) View.VISIBLE else View.GONE

        // Colors
        when (displayStatus) {
            "PENDING" -> holder.tvStatus.setTextColor(Color.parseColor("#FFA500")) // Orange
            "ACCEPTED" -> holder.tvStatus.setTextColor(Color.parseColor("#00FF00")) // Green
            "COMPLETED" -> holder.tvStatus.setTextColor(Color.parseColor("#00BFFF")) // Cool Blue
            "REJECTED" -> holder.tvStatus.setTextColor(Color.parseColor("#FF0000")) // Red
            else -> holder.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
        }

        // Button Clicks
        holder.btnAccept.setOnClickListener { onAcceptClick(apt) }
        holder.btnReject.setOnClickListener { onRejectClick(apt) }
        holder.btnComplete.setOnClickListener { onCompleteClick(apt) }
    }

    override fun getItemCount() = appointments.size

    fun updateList(newList: List<Appointment>) {
        appointments = newList
        notifyDataSetChanged()
    }
}