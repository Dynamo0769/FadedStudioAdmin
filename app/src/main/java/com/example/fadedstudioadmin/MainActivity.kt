package com.example.fadedstudioadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- GLIDE IMAGE LOADING START ---
        val ivHeader = findViewById<ImageView>(R.id.ivLoginHeader)

        // Your specific Google Drive image converted to a direct download link
        val driveLink = "https://drive.google.com/uc?export=download&id=1KY1vo_5KhTB6oMFSbRqejBEfltP2q-gv"

        // Load the image into the header using Glide (Zero red lines now!)
        Glide.with(this)
            .load(driveLink)
            .into(ivHeader)
        // --- GLIDE IMAGE LOADING END ---

        // Linking to your orange and dark UI
        val etEmail = findViewById<EditText>(R.id.etAdminEmail)
        val btnLogin = findViewById<Button>(R.id.btnSendOtp)

        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString().trim()

            if (emailInput.isNotEmpty()) {
                // Queries the Users collection for your email
                db.collection("Users")
                    .whereEqualTo("email", emailInput)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val type = documents.documents[0].getString("userType")

                            if (type == "Admin") {
                                // Access Granted
                                Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, AdminDashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Denied: Not an Admin account", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this, "Email not found in database", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error connecting to Firebase", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}