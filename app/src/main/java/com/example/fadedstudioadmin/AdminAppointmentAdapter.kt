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
    private var userMap: Map<String, String> = emptyMap(),
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit,
    private val onCompleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AdminAppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvService: TextView = view.findViewById(R.id.tvAptService)
        val tvDateTime: TextView = view.findViewById(R.id.tvAptDateTime)
        val tvStatus: TextView = view.findViewById(R.id.tvAptStatus)
        val llActionButtons: LinearLayout = view.findViewById(R.id.llActionButtons)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val btnComplete: Button = view.findViewById(R.id.btnComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val apt = appointments[position]

        // Use the map to find the name. If not found, it shows "Unknown Customer"
        val firstName = userMap[apt.userId] ?: "Unknown Customer"
        holder.tvName.text = firstName

        holder.tvService.text = "Service: ${apt.serviceName}"
        holder.tvDateTime.text = apt.dateTime

        // FIX: Made this more flexible so buttons ALWAYS show for new bookings
        val isPending = apt.status.contains("confirm", ignoreCase = true) || apt.status.contains("Pending", ignoreCase = true)
        val isAccepted = apt.status.equals("Accepted", ignoreCase = true)

        val displayStatus = if (isPending) "PENDING" else apt.status.uppercase()
        holder.tvStatus.text = displayStatus

        // Show/Hide buttons based on real status
        holder.llActionButtons.visibility = if (isPending) View.VISIBLE else View.GONE
        holder.btnComplete.visibility = if (isAccepted) View.VISIBLE else View.GONE

        // Color coding
        when (displayStatus) {
            "PENDING" -> holder.tvStatus.setTextColor(Color.parseColor("#FFA500"))
            "ACCEPTED" -> holder.tvStatus.setTextColor(Color.parseColor("#00FF00"))
            "COMPLETED" -> holder.tvStatus.setTextColor(Color.parseColor("#00BFFF"))
            "REJECTED" -> holder.tvStatus.setTextColor(Color.parseColor("#FF0000"))
        }

        // Click Listeners
        holder.btnAccept.setOnClickListener { onAcceptClick(apt) }
        holder.btnReject.setOnClickListener { onRejectClick(apt) }
        holder.btnComplete.setOnClickListener { onCompleteClick(apt) }
    }

    override fun getItemCount() = appointments.size

    fun updateData(newAppointments: List<Appointment>, newUserMap: Map<String, String>) {
        this.appointments = newAppointments
        this.userMap = newUserMap
        notifyDataSetChanged()
    }
}