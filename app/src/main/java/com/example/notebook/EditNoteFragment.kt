package com.example.notebook


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook.databinding.FragmentEditNoteBinding
import com.example.notebook.model.Constants
import com.example.notebook.model.Note
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class EditNoteFragment : Fragment() {

    lateinit var binding: FragmentEditNoteBinding
    lateinit var  databaseRef : DatabaseReference
    lateinit var note: Note
    lateinit var noteId:String
    lateinit var userId: String
    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_note, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (args.noteId != null){
            noteId = requireNotNull(args.noteId)
            userId = requireNotNull(args.userId)
        }else{
            noteId = requireNotNull(arguments?.getString("noteId"))
            userId = Constants.USER_ID
        }


        if (noteId != null && userId != null) {
            readNoteData(noteId)
        }else{
            findNavController().navigate(R.id.action_editNoteFragment_to_allNotesFragment)
        }
        binding.noteTextET.doAfterTextChanged {
            var newNote = Note(noteId,binding.noteTextET.text.toString())
            editNote(newNote)
            binding.noteTextET.setSelection(binding.noteTextET.text.length)
//            Log.d("EditNoteFragment","text is changed")
        }

        binding.saveNoteBtn.setOnClickListener { view : View ->

            if (binding.noteTextET.text.isNotEmpty()){
                var newNote = Note(noteId,binding.noteTextET.text.toString())
                editNote(newNote)
                Toast.makeText(requireContext(),"your note is updated successfully",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(),"Please write new note firstly",Toast.LENGTH_LONG).show()
            }
        }

        binding.copyNoteLinkBtn.setOnClickListener{view: View ->

            copyNoteLinkToClipboard("example.com/EditNoteFragment/$userId/$noteId")
        }

    }

    private fun copyNoteLinkToClipboard(copyText: String) {
        val clipbroadManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("copy text",copyText)
        clipbroadManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(),"Link is copied successfully",Toast.LENGTH_LONG).show()

    }


    private fun editNote(newNote: Note) {
        databaseRef = FirebaseDatabase.getInstance().getReference("Notes/$userId")
        val childUpdates=HashMap<String,String>()
        childUpdates["id"] = newNote.id
        childUpdates["text"] = newNote.text
        databaseRef.child(newNote.id).updateChildren(childUpdates as Map<String, String>)

    }





    private fun readNoteData(noteId: String){
        binding.loadNoteBP.visibility = View.VISIBLE

        databaseRef = FirebaseDatabase.getInstance().getReference("Notes/$userId/$noteId")

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
