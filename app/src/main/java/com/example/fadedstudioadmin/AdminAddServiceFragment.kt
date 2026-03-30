package com.example.fadedstudioadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddServiceFragment : Fragment() {

    private lateinit var tvHeader: TextView
    private lateinit var etServiceName: EditText
    private lateinit var etServicePrice: EditText
    private lateinit var etServiceDuration: EditText
    private lateinit var etServiceImageUrl: EditText
    private lateinit var btnSaveService: Button
    private lateinit var btnDeleteService: Button

    private val db = FirebaseFirestore.getInstance()

    // We store the ID here if we are editing an existing service
    private var currentServiceId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_service, container, false)

        tvHeader = view.findViewById(R.id.tvEditServicesHeader)
        etServiceName = view.findViewById(R.id.etServiceName)
        etServicePrice = view.findViewById(R.id.etServicePrice)
        etServiceDuration = view.findViewById(R.id.etServiceDuration)
        etServiceImageUrl = view.findViewById(R.id.etServiceImageUrl)
        btnSaveService = view.findViewById(R.id.btnSaveService)
        btnDeleteService = view.findViewById(R.id.btnDeleteService)

        // CHECK IF WE ARE EDITING OR ADDING
        val serviceToEdit = arguments?.getSerializable("SERVICE_DATA") as? Service

        if (serviceToEdit != null) {
            // WE ARE EDITING! Fill in the blanks.
            currentServiceId = serviceToEdit.serviceId
            tvHeader.text = "Edit Service"
            etServiceName.setText(serviceToEdit.serviceName)
            etServicePrice.setText(serviceToEdit.price)
            etServiceDuration.setText(serviceToEdit.duration)
            etServiceImageUrl.setText(serviceToEdit.imageUrl)

            // Show the delete button
            btnDeleteService.visibility = View.VISIBLE
        } else {
            // WE ARE ADDING! Keep it blank.
            tvHeader.text = "Add New Service"
            btnDeleteService.visibility = View.GONE
        }

        btnSaveService.setOnClickListener { saveServiceToFirebase() }
        btnDeleteService.setOnClickListener { deleteServiceFromFirebase() }

        return view
    }

    private fun saveServiceToFirebase() {
        val name = etServiceName.text.toString().trim()
        val price = etServicePrice.text.toString().trim()
        val duration = etServiceDuration.text.toString().trim()
        val imageUrl = etServiceImageUrl.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || duration.isEmpty()) {
            Toast.makeText(context, "Please fill Name, Price, and Duration", Toast.LENGTH_SHORT).show()
            return
        }

        val serviceData = hashMapOf(
            "serviceName" to name,
            "price" to price,
            "duration" to duration,
            "imageUrl" to imageUrl
        )

        if (currentServiceId == null) {
            // ADD NEW
            db.collection("Services").add(serviceData)
                .addOnSuccessListener { goBack("Added successfully!") }
                .addOnFailureListener { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
        } else {
            // UPDATE EXISTING
            db.collection("Services").document(currentServiceId!!).update(serviceData as Map<String, Any>)
                .addOnSuccessListener { goBack("Updated successfully!") }
                .addOnFailureListener { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun deleteServiceFromFirebase() {
        if (currentServiceId != null) {
            db.collection("Services").document(currentServiceId!!).delete()
                .addOnSuccessListener { goBack("Deleted successfully!") }
                .addOnFailureListener { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun goBack(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}