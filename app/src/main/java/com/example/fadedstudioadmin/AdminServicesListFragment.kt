package com.example.fadedstudioadmin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val view = inflater.inflate(R.layout.fragment_admin_services_list, container, false)

        recyclerView = view.findViewById(R.id.rvServices)
        btnAddService = view.findViewById(R.id.btnAddService)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Setup the adapter with the click listener
        adapter = AdminServiceAdapter(serviceList) { clickedService ->
            openAddEditScreen(clickedService)
        }
        recyclerView.adapter = adapter

        // Add Button opens the screen completely blank (null)
        btnAddService.setOnClickListener {
            openAddEditScreen(null)
        }

        fetchServices()
        return view
    }

    private fun openAddEditScreen(serviceToEdit: Service?) {
        val fragment = AdminAddServiceFragment()

        // If we clicked a card, stuff its data into a Bundle to send to the next screen
        if (serviceToEdit != null) {
            val bundle = Bundle()
            bundle.putSerializable("SERVICE_DATA", serviceToEdit)
            fragment.arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace((requireView().parent as ViewGroup).id, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fetchServices() {
        db.collection("Services").addSnapshotListener { snapshots, error ->
            if (error != null) return@addSnapshotListener
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
                    Log.e("FADED_ERROR", "Skipped broken document")
                }
            }
            serviceList.clear()
            serviceList.addAll(newList)
            adapter.updateList(serviceList)
        }
    }
}