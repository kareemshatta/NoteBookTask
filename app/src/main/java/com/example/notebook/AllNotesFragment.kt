package com.example.notebook


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.Interfaces.ClickListnerInterface
import com.example.notebook.adapters.NotesRVAdapter
import com.example.notebook.databinding.FragmentAllNotesBinding
import com.example.notebook.model.Constants
import com.example.notebook.model.Note
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */

class AllNotesFragment : Fragment(),ClickListnerInterface {


    lateinit var noteList :MutableList<Note>
    lateinit var  databaseRef : DatabaseReference
    lateinit var binding: FragmentAllNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_all_notes, container, false)

        databaseRef = FirebaseDatabase.getInstance().getReference("Notes/${Constants.USER_ID}")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNoteBtn.setOnClickListener { view : View ->
            findNavController(view).navigate(R.id.action_allNotesFragment_to_addNoteFragment)
        }

        readNotesData()
    }

    private fun readNotesData(){
        binding.loadNotesBP.visibility = View.VISIBLE
        noteList = mutableListOf()

        databaseRef.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()){
                    noteList.clear()
                    for (note in dataSnapshot.children){
                        noteList.add(requireNotNull(note.getValue(Note::class.java)))
                    }
                    binding.loadNotesBP.visibility = View.GONE
                    binding.notesRecyclerView.layoutManager = LinearLayoutManager(this@AllNotesFragment.context,
                        RecyclerView.VERTICAL,false)
                    val rvAdapter = NotesRVAdapter(this@AllNotesFragment,noteList)
                    binding.notesRecyclerView.adapter = rvAdapter
                }else{
                    binding.loadNotesBP.visibility = View.GONE
                    Toast.makeText(requireContext(),"Can't find any note",Toast.LENGTH_LONG).show()
                    Log.d("NotesActivity_Read", "snapshot:not exist")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                binding.loadNotesBP.visibility = View.GONE
                Toast.makeText(requireContext(),"connection error",Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    override fun noteListItemAction(noteId: String) {
        val bundle = bundleOf("noteId" to noteId)
        findNavController().navigate(R.id.action_allNotesFragment_to_editNoteFragment, bundle)
    }

}
