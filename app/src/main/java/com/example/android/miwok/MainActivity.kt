package com.example.android.miwok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the view pager that will allow the user to swipe between fragments.
        val viewPager = findViewById<ViewPager>(R.id.view_pager)

        // Create an adapter that knows which fragment should be shown on each page
        val categoryAdapter = CategoryAdapter(this, supportFragmentManager)

        // Set the adapter onto the view pager
        viewPager.adapter = categoryAdapter

        // Find the tab layout that shows the tabs
        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager)
    }
}