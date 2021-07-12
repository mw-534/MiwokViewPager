package com.example.android.miwok

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

/**
 * [WordAdapter] is an [ArrayAdapter] that can provide the layout for each list item
 * based on a data source, which is a list of [Word] objects.
 *
 * @param _context is the current context. Used to inflate the layout file.
 * @param words is a list of [Word] objects to display in a list.
 * @param colorResourceId is the resource ID for the background color for this list of words.
 */
class WordAdapter(private val _context: Context, private val words: ArrayList<Word>, private val colorResourceId: Int) : ArrayAdapter<Word>(_context,0, words) {
    // The ArrayAdapter is not going to use the second argument so it can be any value. Here we use 0.

    /**
     * Provides a view for an AdapterView (ListView, GridView etc.)
     *
     * @param position is the position in the list of data that should be displayed in the list item view.
     * @param convertView is the recycled view to populate.
     * @param parent is the parent viewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Check if the existing view is being reused, otherwise inflate the view and set color.
        val listItemView: View
        if (convertView == null) {
            // Inflate view.
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            // Set the theme color for the list item.
            val textContainer = listItemView.findViewById<RelativeLayout>(R.id.text_container)
            // Find the color that the resource ID maps to. Use ContextCompat instead of context.getColor() here because context.getColor() requires API 23 while minimum API for this app is 16.
            val color = ContextCompat.getColor(context, colorResourceId)
            // Set background color of text container.
            textContainer.setBackgroundColor(color)
        } else {
            // Reuse existing view.
            listItemView = convertView
        }

        // Get the [Word] object located at this position in the list.
        val currentWord = getItem(position)

        // Set default translation.
        val tvDefault = listItemView.findViewById<TextView>(R.id.default_text_view)
        tvDefault.text = currentWord?.defaultTranslation

        // Set Miwok translation.
        val tvMiwok = listItemView.findViewById<TextView>(R.id.miwok_text_view)
        tvMiwok.text = currentWord?.miwokTranslation

        // Set image resource or hide image.
        val imageView = listItemView.findViewById<ImageView>(R.id.image)
        if (currentWord?.hasImage() == true) {
            imageView.setImageResource(currentWord.imageResourceId)
            // Needs to be set to VISIBLE because the view gets reused.
            imageView.visibility = View.VISIBLE
        } else {
            // Hide image view.
            imageView.visibility = View.GONE
        }

        return listItemView
    }
}