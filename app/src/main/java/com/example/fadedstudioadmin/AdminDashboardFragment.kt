package com.example.fadedstudioadmin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardFragment : Fragment() {

    private lateinit var tvPendingCount: TextView
    private lateinit var tvTotalRevenue: TextView
    private lateinit var tvClientCount: TextView
    private lateinit var tvServiceCount: TextView
    private lateinit var rvClients: RecyclerView
    private lateinit var adapter: AdminClientAdapter

    private val db = FirebaseFirestore.getInstance()
    private val servicePrices = mutableMapOf<String, Double>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false)

        tvPendingCount = view.findViewById(R.id.tvPendingCount)
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue)
        tvClientCount = view.findViewById(R.id.tvClientCount)
        tvServiceCount = view.findViewById(R.id.tvServiceCount)
        rvClients = view.findViewById(R.id.rvClients)

        rvClients.layoutManager = LinearLayoutManager(context)
        adapter = AdminClientAdapter(emptyList())
        rvClients.adapter = adapter

        fetchDashboardData()

        return view
    }

    private fun fetchDashboardData() {
        // THE FIX: Changed collection name from "Users" to "users" (lowercase)
        // to match your DB with the phone numbers
        db.collection("users").addSnapshotListener { snapshots, error ->
            if (error != null || snapshots == null) return@addSnapshotListener

            val userList = mutableListOf<User>()
            for (doc in snapshots.documents) {
                val firstName = doc.getString("firstName") ?: ""
                val lastName = doc.getString("lastName") ?: ""

                val fullName = if (firstName.isNotBlank() || lastName.isNotBlank()) {
                    "$firstName $lastName".trim()
                } else {
                    "Unknown User"
                }

                val email = doc.getString("email") ?: "No Email"

                // Now grabbing the actual "phone" field from your lowercase 'users' collection
                val phone = doc.getString("phone") ?: "N/A"

                userList.add(User(doc.id, fullName, email, phone))
            }
            tvClientCount.text = userList.size.toString()
            adapter.updateList(userList)
        }

        // Fetch Services for revenue calculation
        db.collection("Services").addSnapshotListener { snapshots, error ->
            if (error != null || snapshots == null) return@addSnapshotListener
            tvServiceCount.text = snapshots.size().toString()
            servicePrices.clear()
            for (doc in snapshots.documents) {
                val serviceName = doc.getString("serviceName") ?: ""
                val priceString = doc.getString("price") ?: "0"
                val price = priceString.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                if (serviceName.isNotEmpty()) {
                    servicePrices[serviceName] = price
                }
            }
            fetchAppointmentsData()
        }
    }

    private fun fetchAppointmentsData() {
        db.collection("Appointments").addSnapshotListener { snapshots, error ->
            if (error != null || snapshots == null) return@addSnapshotListener
            var pendingCount = 0
            var totalRevenue = 0.0
            for (doc in snapshots.documents) {
                val status = doc.getString("status") ?: ""
                val serviceName = doc.getString("serviceName") ?: ""
                if (status.equals("Pending", true) || status.equals("To be confirm", true)) {
                    pendingCount++
                } else if (status.equals("Completed", true)) {
                    val price = servicePrices[serviceName] ?: 0.0
                    totalRevenue += price
                }
            }
            tvPendingCount.text = pendingCount.toString()
            tvTotalRevenue.text = if (totalRevenue % 1.0 == 0.0) "₱${totalRevenue.toInt()}" else "₱${String.format("%.2f", totalRevenue)}"
        }
    }
}