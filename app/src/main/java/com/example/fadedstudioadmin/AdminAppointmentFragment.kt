package com.example.fadedstudioadmin

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

    private lateinit var rvAppointments: RecyclerView
    private lateinit var btnPending: Button
    private lateinit var btnAccepted: Button
    private lateinit var btnCompleted: Button
    private lateinit var btnRejected: Button
    private lateinit var btnAll: Button

    private lateinit var adapter: AdminAppointmentAdapter
    private val db = FirebaseFirestore.getInstance()
    private val allAppointments = mutableListOf<Appointment>()
    private val userMap = mutableMapOf<String, String>()
    private var currentFilter = "Pending"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_appointments, container, false)

        rvAppointments = view.findViewById(R.id.rv_appointments)
        btnPending = view.findViewById(R.id.filter_pending)
        btnAccepted = view.findViewById(R.id.filter_accepted)
        btnCompleted = view.findViewById(R.id.filter_completed)
        btnRejected = view.findViewById(R.id.filter_rejected)
        btnAll = view.findViewById(R.id.filter_all)

        rvAppointments.layoutManager = LinearLayoutManager(context)

        adapter = AdminAppointmentAdapter(
            emptyList(),
            userMap,
            onAcceptClick = { apt -> updateStatusInFirebase(apt, "Accepted") },
            onRejectClick = { apt -> updateStatusInFirebase(apt, "Rejected") },
            onCompleteClick = { apt -> updateStatusInFirebase(apt, "Completed") }
        )
        rvAppointments.adapter = adapter

        btnPending.setOnClickListener { setFilter("Pending") }
        btnAccepted.setOnClickListener { setFilter("Accepted") }
        btnCompleted.setOnClickListener { setFilter("Completed") }
        btnRejected.setOnClickListener { setFilter("Rejected") }
        btnAll.setOnClickListener { setFilter("All") }

        fetchAllUserNames()

        return view
    }

    private fun fetchAllUserNames() {
        // 1. Check 'users' (small u)
        db.collection("users").addSnapshotListener { snapshots, _ ->
            snapshots?.documents?.forEach { doc ->
                val name = doc.getString("firstName") ?: "Customer"
                userMap[doc.id] = name
            }

            // 2. ALSO Check 'Users' (Big U) for names
            db.collection("Users").addSnapshotListener { snapshots2, _ ->
                snapshots2?.documents?.forEach { doc ->
                    // Some use 'firstName', some might use 'name' or 'fullName'
                    val name = doc.getString("firstName") ?: doc.getString("name") ?: "Customer"
                    userMap[doc.id] = name
                }

                // Now that we've checked BOTH places, get the appointments
                fetchAppointments()
            }
        }
    }

    private fun fetchAppointments() {
        db.collection("Appointments").addSnapshotListener { snapshots, error ->
            if (error != null) return@addSnapshotListener

            allAppointments.clear()
            snapshots?.documents?.forEach { document ->
                try {
                    val apt = Appointment(
                        appointmentId = document.id,
                        userId = document.getString("userId") ?: "",
                        serviceName = document.getString("serviceName") ?: "Unknown Service",
                        dateTime = document.getString("dateTime") ?: "No Date",
                        status = document.getString("status") ?: "Pending"
                    )
                    allAppointments.add(apt)
                } catch (e: Exception) {}
            }
            applyFilter()
        }
    }

    private fun updateStatusInFirebase(appointment: Appointment, newStatus: String) {
        db.collection("Appointments").document(appointment.appointmentId)
            .update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(context, "Status: $newStatus", Toast.LENGTH_SHORT).show()
                setFilter(newStatus)
            }
    }

    private fun setFilter(filterType: String) {
        currentFilter = filterType
        val activeColor = ColorStateList.valueOf(Color.parseColor("#D4AF37"))
        val inactiveColor = ColorStateList.valueOf(Color.parseColor("#333333"))

        btnPending.backgroundTintList = if (filterType == "Pending") activeColor else inactiveColor
        btnAccepted.backgroundTintList = if (filterType == "Accepted") activeColor else inactiveColor
        btnCompleted.backgroundTintList = if (filterType == "Completed") activeColor else inactiveColor
        btnRejected.backgroundTintList = if (filterType == "Rejected") activeColor else inactiveColor
        btnAll.backgroundTintList = if (filterType == "All") activeColor else inactiveColor

        applyFilter()
    }

    private fun applyFilter() {
        val filteredList = when (currentFilter) {
            "All" -> allAppointments
            "Pending" -> allAppointments.filter { it.status.contains("Pending", true) || it.status.contains("confirm", true) }
            else -> allAppointments.filter { it.status.equals(currentFilter, true) }
        }
        adapter.updateData(filteredList, userMap)
    }
}