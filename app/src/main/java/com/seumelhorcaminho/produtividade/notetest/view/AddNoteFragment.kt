package com.seumelhorcaminho.produtividade.notetest.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.seumelhorcaminho.produtividade.notetest.MainActivity
import com.seumelhorcaminho.produtividade.notetest.R
import com.seumelhorcaminho.produtividade.notetest.databinding.FragmentAddNoteBinding
import com.seumelhorcaminho.produtividade.notetest.databinding.FragmentHomeBinding
import com.seumelhorcaminho.produtividade.notetest.model.Note
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModel


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.etTitle.text.toString().trim()
        val noteDescription = binding.etDescription.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDescription)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Nota salva", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)

        } else {
            Toast.makeText(addNoteView.context, "Verifique os dados", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.add_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_save -> {
                saveNote(addNoteView)
                true
            }

            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }

}