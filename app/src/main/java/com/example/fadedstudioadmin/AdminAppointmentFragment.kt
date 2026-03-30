package com.example.fadedstudioadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminAppointmentFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val fullList = mutableListOf<Appointment>()
    private lateinit var adapter: AdminAppointmentAdapter
    private var firebaseListener: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_appointments, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rv_appointments)
        rv.layoutManager = LinearLayoutManager(context)

        adapter = AdminAppointmentAdapter(fullList) { appointment, newStatus ->
            updateAppointmentStatus(appointment, newStatus)
        }
        rv.adapter = adapter

        startListening()
        return view
    }

    private fun startListening() {
        firebaseListener = db.collection("Appointments")
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener

                fullList.clear()
                snapshots?.forEach { doc ->
                    val appt = doc.toObject(Appointment::class.java)
                    appt.id = doc.id
                    fullList.add(appt)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun updateAppointmentStatus(appt: Appointment, status: String) {
        val docId = appt.id ?: return
        appt.isUpdating = true
        adapter.notifyDataSetChanged()

        db.collection("Appointments").document(docId)
            .update("status", status)
            .addOnSuccessListener {
                appt.isUpdating = false
                Toast.makeText(context, "Appointment $status", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                appt.isUpdating = false
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseListener?.remove()
    }
}