package com.example.notebook


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.adapters.NotesRVAdapter
import com.example.notebook.databinding.FragmentEditNoteBinding
import com.example.notebook.model.Note
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class EditNoteFragment : Fragment() {

    lateinit var binding: FragmentEditNoteBinding
    lateinit var  databaseRef : DatabaseReference
    lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_note, container, false)


//        binding.addNoteBtn.setOnClickListener { view : View ->
//            Navigation.findNavController(view).navigate(R.id.action_allNotesFragment_to_addNoteFragment)
//        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = requireNotNull(arguments?.getString("noteId"))
        readNoteData(noteId)

        binding.saveNoteBtn.setOnClickListener { view : View ->

            if (binding.noteTextET.text.isNotEmpty()){
                var newNote = Note(noteId,binding.noteTextET.text.toString())
                editNote(newNote)
            }else{
                Toast.makeText(requireContext(),"Please write new note firstly",Toast.LENGTH_LONG).show()
            }
        }

    }



    private fun editNote(newNote: Note) {
        databaseRef = FirebaseDatabase.getInstance().getReference("Notes")
        val childUpdates=HashMap<String,String>()
        childUpdates["id"] = newNote.id
        childUpdates["text"] = newNote.text
        databaseRef.child(newNote.id).updateChildren(childUpdates as Map<String, String>)
        Toast.makeText(requireContext(),"your note is updated successfully",Toast.LENGTH_LONG).show()

    }





    private fun readNoteData(noteId: String){
        binding.loadNoteBP.visibility = View.VISIBLE

        databaseRef = FirebaseDatabase.getInstance().getReference("Notes/$noteId")

        databaseRef.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) =
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()){
                    val id = requireNotNull(dataSnapshot.children.elementAt(0).getValue(String::class.java))
                    val text = requireNotNull(dataSnapshot.children.elementAt(1).getValue(String::class.java))
                    note = Note(id,text)
                    binding.noteTextET.setText(note.text)
//                    Toast.makeText(requireContext(),id +" ==== "+text,Toast.LENGTH_LONG).show()
                    binding.loadNoteBP.visibility = View.GONE

                }else{
                    binding.loadNoteBP.visibility = View.VISIBLE
//                    Log.d("NotesActivity_Read", "snapshot:not exist")
                }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                binding.loadNoteBP.visibility = View.VISIBLE
//                Log.d("NotesActivity_Read", "loadPost:onCancelled"+databaseError.message)
            }
        }
        )
    }


}
