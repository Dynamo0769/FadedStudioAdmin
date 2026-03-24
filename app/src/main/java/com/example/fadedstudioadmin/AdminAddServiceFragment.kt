package com.example.fadedstudioadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddServiceFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_service, container, false)

        val etName = view.findViewById<EditText>(R.id.etServiceName)
        val etDetail = view.findViewById<EditText>(R.id.etServiceDetail)
        val etPrice = view.findViewById<EditText>(R.id.etServicePrice)
        val etDuration = view.findViewById<EditText>(R.id.etServiceDuration)
        val btnSave = view.findViewById<Button>(R.id.btnSaveService)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty()) {
                val service = hashMapOf(
                    "serviceName" to name,
                    "detail" to etDetail.text.toString(),
                    "price" to price,
                    "duration" to etDuration.text.toString()
                )
                db.collection("Services").add(service).addOnSuccessListener {
                    Toast.makeText(context, "Service Added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}