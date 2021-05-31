package com.example.notebook.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.R
import com.example.notebook.model.Note


class NotesRVAdapter(private val notesList: MutableList<Note>) :
    RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var noteTV: TextView

        init {
            // Define click listener for the ViewHolder's View.
            noteTV = view.findViewById(R.id.note_text_TV) as TextView
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.noteTV.text = notesList.get(position).text
        viewHolder.noteTV.setOnClickListener(){
            val bundle = bundleOf("noteId" to notesList.get(position).id.toString())
            it.findNavController().navigate(R.id.action_allNotesFragment_to_editNoteFragment, bundle)
        }

    }

    // Return the size of your employeesList (invoked by the layout manager)
    override fun getItemCount() = notesList.size

}
