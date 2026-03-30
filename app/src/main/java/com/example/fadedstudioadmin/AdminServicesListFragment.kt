package com.example.fadedstudioadmin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class AdminServicesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        try {
            // MATCHES YOUR XML EXACTLY
            val view = inflater.inflate(R.layout.fragment_admin_services_list, container, false)
            return view

        } catch (e: Exception) {
            Log.e("FADED_ERROR", "Service Fragment Crashed: ${e.message}")
            Toast.makeText(context, "Safe Mode: Service Screen Error Prevented", Toast.LENGTH_LONG).show()
            return View(context)
        }
    }
}