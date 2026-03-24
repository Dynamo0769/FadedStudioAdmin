package com.example.fadedstudioadmin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val bottomNav = findViewById<BottomNavigationView>(R.id.adminBottomNav)

        // Start with Appointments screen as default
        replaceFragment(AdminAppointmentFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_services -> replaceFragment(AdminAddServiceFragment())
                R.id.nav_appointments -> replaceFragment(AdminAppointmentFragment())
                // Add Dashboard and Profile fragments here later
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .commit()
    }
}