package com.example.fadedstudioadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminAppointmentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminAppointmentAdapter
    private var allAppointments = mutableListOf<Appointment>()
    private val db = FirebaseFirestore.getInstance()
    private var currentFilter = "Pending"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_appointments, container, false)

        recyclerView = view.findViewById(R.id.rv_appointments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = AdminAppointmentAdapter(emptyList()) { appointmentId, newStatus ->
            updateAppointmentStatus(appointmentId, newStatus)
        }
        recyclerView.adapter = adapter

        val btnAll: Button = view.findViewById(R.id.filter_all)
        val btnPending: Button = view.findViewById(R.id.filter_pending)
        val btnAccepted: Button = view.findViewById(R.id.filter_accepted)
        val btnRejected: Button = view.findViewById(R.id.filter_rejected)

        btnAll.setOnClickListener { applyFilter("All") }
        btnPending.setOnClickListener { applyFilter("Pending") }
        btnAccepted.setOnClickListener { applyFilter("Accepted") }
        btnRejected.setOnClickListener { applyFilter("Rejected") }

        fetchAppointments()

        return view
    }

    private fun fetchAppointments() {
        db.collection("Appointments").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            allAppointments.clear()
            for (document in snapshot.documents) {
                val appt = document.toObject(Appointment::class.java)
                if (appt != null) {
                    if (appt.appointmentId.isEmpty()) {
                        appt.appointmentId = document.id
                    }
                    allAppointments.add(appt)
                }
            }
            applyFilter(currentFilter)
        }
    }

    private fun applyFilter(status: String) {
        currentFilter = status
        val filteredList = if (status == "All") {
            allAppointments
        } else {
            allAppointments.filter { it.status.equals(status, ignoreCase = true) }
        }

        adapter.updateList(filteredList)
    }

    private fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        val index = allAppointments.indexOfFirst { it.appointmentId == appointmentId }
        if (index != -1) {
            allAppointments[index].status = newStatus
            applyFilter(currentFilter)
        }

        db.collection("Appointments").document(appointmentId)
            .update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Moved to $newStatus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error updating database", Toast.LENGTH_SHORT).show()
            }
    }
}