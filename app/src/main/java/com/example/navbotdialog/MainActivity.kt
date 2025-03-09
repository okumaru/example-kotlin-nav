package com.example.navbotdialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.navbotdialog.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                finish()
            }
        })

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_bottom_home -> openFragment(HomeFragment())
                R.id.nav_bottom_shorts -> openFragment(ShortFragment())
                R.id.nav_bottom_subscriptions -> openFragment(SubscriptionsFragment())
                R.id.nav_bottom_library -> openFragment(LibraryFragment())
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

        binding.fab.setOnClickListener{
            showBottomDialog()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> openFragment(HomeFragment())
            R.id.nav_settings -> openFragment(SettingsFragment())
            R.id.nav_share -> openFragment(ShareFragment())
            R.id.nav_about -> openFragment(AboutFragment())
            R.id.nav_logout -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction")
    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun showBottomDialog() {
        val dialogs = Dialog(this)
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogs.setContentView(R.layout.bottomsheetlayout)

        val videoLayout: LinearLayout = dialogs.findViewById(R.id.layoutVideo)
        val shortsLayout: LinearLayout = dialogs.findViewById(R.id.layoutShorts)
        val liveLayout: LinearLayout = dialogs.findViewById(R.id.layoutLive)

        videoLayout.setOnClickListener {
            dialogs.dismiss()
            openFragment(VideoFragment())
        }

        shortsLayout.setOnClickListener {
            dialogs.dismiss()
            openFragment(ShortFragment())
        }

        liveLayout.setOnClickListener {
            dialogs.dismiss()
            openFragment(LiveFragment())
        }

        dialogs.show()
        dialogs.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogs.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialogs.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialogs.window?.setGravity(Gravity.BOTTOM)
    }
}