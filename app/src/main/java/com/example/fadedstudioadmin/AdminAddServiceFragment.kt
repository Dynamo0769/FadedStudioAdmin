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
        // Use the XML layout you actually have
        val view = inflater.inflate(R.layout.fragment_admin_edit_service, container, false)

        // Link the specific IDs from your XML
        val etImageUrl = view.findViewById<EditText>(R.id.etServiceImageUrl)
        val etName = view.findViewById<EditText>(R.id.etServiceName)
        val etDetail = view.findViewById<EditText>(R.id.etServiceDetail)
        val etPrice = view.findViewById<EditText>(R.id.etServicePrice)
        val etDuration = view.findViewById<EditText>(R.id.etServiceDuration)
        val btnSave = view.findViewById<Button>(R.id.btnSaveService)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val price = etPrice.text.toString().trim()

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "Name and Price are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a map of the data to send to Firebase
            val serviceData = hashMapOf(
                "name" to name,
                "price" to price,
                "detail" to etDetail.text.toString().trim(),
                "imageUrl" to etImageUrl.text.toString().trim(),
                "duration" to etDuration.text.toString().trim()
            )

            db.collection("Services")
                .add(serviceData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Service Added Successfully!", Toast.LENGTH_SHORT).show()
                    // Go back to the list after saving
                    parentFragmentManager.popBackStack()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}