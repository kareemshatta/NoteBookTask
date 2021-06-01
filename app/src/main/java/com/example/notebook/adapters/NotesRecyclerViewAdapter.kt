package com.example.notebook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.AllNotesFragment
import com.example.notebook.R
import com.example.notebook.model.Note


class NotesRecyclerViewAdapter(private val fragment: AllNotesFragment, private val notesList: MutableList<Note>) :
    RecyclerView.Adapter<NotesRecyclerViewAdapter.NoteViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var noteTV: TextView

        init {
            // Define click listener for the ViewHolder's View.
            noteTV = view.findViewById(R.id.note_text_TextView) as TextView
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NoteViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_row_item, viewGroup, false)

        return NoteViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(noteViewHolder: NoteViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        noteViewHolder.noteTV.text = notesList.get(position).text
        noteViewHolder.noteTV.setOnClickListener(){
            fragment.onNoteClicked(notesList.get(position).id)
        }

    }

    // Return the size of your employeesList (invoked by the layout manager)
    override fun getItemCount() = notesList.size

}
