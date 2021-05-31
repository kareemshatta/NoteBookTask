package com.example.notebook


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.notebook.databinding.FragmentAddNoteBinding
import com.example.notebook.model.Note
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class AddNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddNoteBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_note, container, false)

//        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveNoteBtn.setOnClickListener { view : View ->
            if (binding.noteTextET.text.isNotEmpty()){
                var newNote = Note("",binding.noteTextET.text.toString())
                saveNote(newNote)
            }else{
                Toast.makeText(requireContext(),"Please write new note firstly",Toast.LENGTH_LONG).show()
            }

        }

        return binding.root
    }


    private fun saveNote(newNote: Note) {
        val database = FirebaseDatabase.getInstance().getReference("Notes")
        newNote.id = database.push().key ?: ""
        database.child(newNote.id).setValue(newNote).addOnCompleteListener(){
            findNavController().navigate(R.id.action_addNoteFragment_to_allNotesFragment)
//            Toast.makeText(requireContext(),"new note is saved",Toast.LENGTH_LONG).show()
        }

    }


}
