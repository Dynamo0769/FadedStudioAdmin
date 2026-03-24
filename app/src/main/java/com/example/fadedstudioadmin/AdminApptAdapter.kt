package com.example.fadedstudioadmin

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

// Renamed to ApptViewHolder to avoid ghost conflicts
class ApptViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvRef: TextView = view.findViewById(R.id.tvAdminApptRef)
    val tvService: TextView = view.findViewById(R.id.tvAdminApptService)
    val tvStatus: TextView = view.findViewById(R.id.tvAdminApptStatus)
    val btnAccept: Button = view.findViewById(R.id.btnAcceptAppt)
    val btnReject: Button = view.findViewById(R.id.btnRejectAppt)
}

// Renamed to AdminApptAdapter to bypass the Redeclaration error
class AdminApptAdapter(
    private var apptList: List<Appointment>,
    private val onStatusUpdate: (String, String) -> Unit
) : RecyclerView.Adapter<ApptViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApptViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_appointment, parent, false)
        return ApptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApptViewHolder, position: Int) {
        val appt = apptList[position]

        val displayId = if (appt.appointmentId.length >= 5) appt.appointmentId.takeLast(5) else "N/A"

        holder.tvRef.text = "Ref: ${displayId.uppercase()}"
        holder.tvService.text = appt.serviceName
        holder.tvStatus.text = "Status: ${appt.status.uppercase()}"

        if (appt.status.equals("Pending", ignoreCase = true)) {
            holder.btnAccept.visibility = View.VISIBLE
            holder.btnReject.visibility = View.VISIBLE
        } else {
            holder.btnAccept.visibility = View.GONE
            holder.btnReject.visibility = View.GONE
        }

        holder.btnAccept.setOnClickListener { onStatusUpdate(appt.appointmentId, "Accepted") }
        holder.btnReject.setOnClickListener { onStatusUpdate(appt.appointmentId, "Rejected") }
    }

    override fun getItemCount() = apptList.size

    fun updateList(newList: List<Appointment>) {
        apptList = newList
        notifyDataSetChanged()
    }
}