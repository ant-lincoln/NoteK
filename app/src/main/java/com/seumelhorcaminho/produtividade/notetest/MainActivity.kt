package com.seumelhorcaminho.produtividade.notetest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.seumelhorcaminho.produtividade.notetest.database.NoteDatabase
import com.seumelhorcaminho.produtividade.notetest.databinding.ActivityMainBinding
import com.seumelhorcaminho.produtividade.notetest.repository.NoteRepository
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModel
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupViewModel()
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory)[NoteViewModel::class.java]

    }
}
