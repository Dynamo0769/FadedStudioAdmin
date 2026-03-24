package com.example.fadedstudioadmin

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

// ViewHolder is OUTSIDE the main class to prevent any nesting errors
class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvRef: TextView = view.findViewById(R.id.tvAdminApptRef)
    val tvService: TextView = view.findViewById(R.id.tvAdminApptService)
    val tvStatus: TextView = view.findViewById(R.id.tvAdminApptStatus)
    val btnAccept: Button = view.findViewById(R.id.btnAcceptAppt)
    val btnReject: Button = view.findViewById(R.id.btnRejectAppt)
}

class AdminAppointmentAdapter(
    private var apptList: List<Appointment>,
    private val onStatusUpdate: (String, String) -> Unit
) : RecyclerView.Adapter<AdminViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_appointment, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val appt = apptList[position]

        val displayId = if (appt.appointmentId.length >= 5) appt.appointmentId.takeLast(5) else "N/A"

        holder.tvRef.text = "Ref: ${displayId.uppercase()}"
        holder.tvService.text = appt.serviceName
        holder.tvStatus.text = "Status: ${appt.status.uppercase()}"

        // Hide buttons if already Accepted/Rejected
        if (appt.status.equals("Pending", ignoreCase = true)) {
            holder.btnAccept.visibility = View.VISIBLE
            holder.btnReject.visibility = View.VISIBLE
        } else {
            holder.btnAccept.visibility = View.GONE
            holder.btnReject.visibility = View.GONE
        }

        // Trigger the instant-move logic
        holder.btnAccept.setOnClickListener { onStatusUpdate(appt.appointmentId, "Accepted") }
        holder.btnReject.setOnClickListener { onStatusUpdate(appt.appointmentId, "Rejected") }
    }

    override fun getItemCount() = apptList.size

    fun updateList(newList: List<Appointment>) {
        apptList = newList
        notifyDataSetChanged()
    }
}