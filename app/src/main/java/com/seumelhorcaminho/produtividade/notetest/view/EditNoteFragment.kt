package com.seumelhorcaminho.produtividade.notetest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.seumelhorcaminho.produtividade.notetest.MainActivity
import com.seumelhorcaminho.produtividade.notetest.R
import com.seumelhorcaminho.produtividade.notetest.databinding.FragmentEditNoteBinding
import com.seumelhorcaminho.produtividade.notetest.model.Note
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.etTitleUpdate.setText(currentNote.noteTitle)
        binding.etDescriptionUpdate.setText(currentNote.noteDesc)

        binding.fabUpdate.setOnClickListener {
            val noteTitle = binding.etTitleUpdate.text.toString().trim()
            val noteDescription = binding.etDescriptionUpdate.text.toString().trim()

            if (noteTitle.isNotEmpty()) {
                val note = Note(currentNote.id, noteTitle, noteDescription)
                notesViewModel.updateNote(note)
//                view.findNavController().popBackStack(R.id.homeFragment, false)
                Toast.makeText(context, "Nota atualizada", Toast.LENGTH_SHORT).show()
                view.findNavController().navigate(
                    R.id.action_editNoteFragment_to_homeFragment
                )

            } else {
                Toast.makeText(context, "Entre com o título", Toast.LENGTH_SHORT).show()
            }
        }

        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_note_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteNote()
//                        Toast.makeText(requireContext(), "Botão delete pressionado", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteNote(){
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Excluir nota")
            setMessage("Deseja excluir esta nota?")
            setPositiveButton("Excluir"){_,_ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context,"Nota excluída", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment,false)
            }
            setNegativeButton("Cancelar", null)
        }.create().show()
    }

//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menu.clear()
//        menuInflater.inflate(R.menu.edit_note_menu, menu)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        return when(menuItem.itemId){
//            R.id.menu_delete -> {
//                deleteNote()
//                true
//            }else -> false
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }


}