package com.example.android.miwok

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * [CategoryAdapter] is a [FragmentPagerAdapter] that can provide the layout for
 * each list item based on a data source which is a list of [Word] objects.
 *
 * @constructor Creates a new [CategoryAdapter] object.
 * @property context is the context of the app.
 * @param fm is the fragment manager that will keep each fragment's state in the adapter
 *           across swipes.
 */
class CategoryAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    /**
     * Return the Fragment associated with a specified position.
     */
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NumbersFragment()
            1 -> FamilyFragment()
            2 -> ColorsFragment()
            3 -> PhrasesFragment()
            else -> NumbersFragment()
        }
    }

    /**
     * Return the number of views available.
     */
    override fun getCount() = 4

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position.
        return when (position) {
            0 -> context.getString(R.string.category_numbers)
            1 -> context.getString(R.string.category_family)
            2 -> context.getString(R.string.category_colors)
            else -> context.getString(R.string.category_phrases)
        }
    }
}