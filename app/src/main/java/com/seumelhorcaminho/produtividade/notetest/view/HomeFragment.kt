package com.seumelhorcaminho.produtividade.notetest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.seumelhorcaminho.produtividade.notetest.MainActivity
import com.seumelhorcaminho.produtividade.notetest.R
import com.seumelhorcaminho.produtividade.notetest.adapter.NoteAdapter
import com.seumelhorcaminho.produtividade.notetest.databinding.FragmentHomeBinding
import com.seumelhorcaminho.produtividade.notetest.model.Note
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModel


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        notesViewModel = (activity as MainActivity).noteViewModel
        setupHomeRecyclerView()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
                val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
                mMenuSearch.isSubmitButtonEnabled = true
                mMenuSearch.setOnQueryTextListener(this@HomeFragment)


                val searchItem = menu.findItem(R.id.menu_search)
                val searchView = searchItem.actionView as SearchView

                searchView.queryHint = "Ex: Lista de compras"
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> activity?.finish()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query%"

        notesViewModel.searchNote(searchQuery).observe(this) { list ->
            noteAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchNote(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menu.clear()
//        menuInflater.inflate(R.menu.home_menu, menu)
//
//        val menuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
//        menuSearch.isSubmitButtonEnabled = false
//        menuSearch.setOnQueryTextListener(this)
//
//        menuSearch.queryHint = "Ex: Lista de compras"
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        return false
//    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }
}