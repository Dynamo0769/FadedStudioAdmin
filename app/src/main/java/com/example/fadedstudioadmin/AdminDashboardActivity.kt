package com.example.fadedstudioadmin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val nav = findViewById<BottomNavigationView>(R.id.adminBottomNav)

        if (savedInstanceState == null) {
            replaceFragment(AdminAppointmentFragment())
        }

        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> replaceFragment(AdminDashboardFragment())
                R.id.nav_services -> replaceFragment(AdminServicesListFragment())
                R.id.nav_appointments -> replaceFragment(AdminAppointmentFragment())
                R.id.nav_profile -> replaceFragment(AdminProfileFragment())
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