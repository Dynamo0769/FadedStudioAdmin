package com.example.fadedstudioadmin

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

class AdminServicesListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddService: Button
    private lateinit var adapter: AdminServiceAdapter
    private val db = FirebaseFirestore.getInstance()
    private val serviceList = mutableListOf<Service>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // MAKE SURE THIS MATCHES YOUR XML NAME EXACTLY
        val view = inflater.inflate(R.layout.fragment_admin_services_list, container, false)

        recyclerView = view.findViewById(R.id.rvServices)
        btnAddService = view.findViewById(R.id.btnAddService)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AdminServiceAdapter(serviceList)
        recyclerView.adapter = adapter

        btnAddService.setOnClickListener {
            Toast.makeText(context, "Add Service Clicked!", Toast.LENGTH_SHORT).show()
        }

        fetchServices()

        return view
    }

    private fun fetchServices() {
        db.collection("Services").addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.e("FADED_ERROR", "Error fetching services", error)
                return@addSnapshotListener
            }

            val newList = mutableListOf<Service>()

            snapshots?.documents?.forEach { document ->
                try {
                    val service = Service(
                        serviceId = document.id,
                        serviceName = document.getString("serviceName") ?: "",
                        price = document.getString("price") ?: "0",
                        duration = document.getString("duration") ?: "",
                        imageUrl = document.getString("imageUrl") ?: ""
                    )
                    newList.add(service)
                } catch (e: Exception) {
                    Log.e("FADED_ERROR", "Skipped broken document: ${document.id}")
                }
            }

            serviceList.clear()
            serviceList.addAll(newList)
            adapter.updateList(serviceList)
        }
    }
}