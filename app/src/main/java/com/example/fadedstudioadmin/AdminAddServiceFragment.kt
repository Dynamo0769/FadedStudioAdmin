package com.example.fadedstudioadmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddServiceFragment : Fragment() {

    // Linking to your XML IDs
    private lateinit var etServiceName: EditText
    private lateinit var etServicePrice: EditText
    private lateinit var etServiceDuration: EditText
    private lateinit var etServiceImageUrl: EditText
    private lateinit var btnSaveService: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout you provided
        val view = inflater.inflate(R.layout.fragment_admin_edit_service, container, false)

        etServiceName = view.findViewById(R.id.etServiceName)
        etServicePrice = view.findViewById(R.id.etServicePrice)
        etServiceDuration = view.findViewById(R.id.etServiceDuration)
        etServiceImageUrl = view.findViewById(R.id.etServiceImageUrl)
        btnSaveService = view.findViewById(R.id.btnSaveService)

        // What happens when you click Save
        btnSaveService.setOnClickListener {
            saveServiceToFirebase()
        }

        return view
    }

    private fun saveServiceToFirebase() {
        // Grab the text the user typed
        val name = etServiceName.text.toString().trim()
        val price = etServicePrice.text.toString().trim()
        val duration = etServiceDuration.text.toString().trim()
        val imageUrl = etServiceImageUrl.text.toString().trim()

        // Don't let them save if the important stuff is blank
        if (name.isEmpty() || price.isEmpty() || duration.isEmpty()) {
            Toast.makeText(context, "Please fill in Name, Price, and Duration", Toast.LENGTH_SHORT).show()
            return
        }

        // Package it exactly how your Firebase is structured
        val serviceData = hashMapOf(
            "serviceName" to name,
            "price" to price,
            "duration" to duration,
            "imageUrl" to imageUrl
        )

        // Push it to Firebase!
        db.collection("Services")
            .add(serviceData)
            .addOnSuccessListener {
                Toast.makeText(context, "New Cut Added Successfully!", Toast.LENGTH_SHORT).show()
                // Simulate hitting the "Back" button to return to the list screen
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error saving: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}