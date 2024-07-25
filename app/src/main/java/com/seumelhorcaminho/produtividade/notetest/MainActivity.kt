package com.seumelhorcaminho.produtividade.notetest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.seumelhorcaminho.produtividade.notetest.database.NoteDatabase
import com.seumelhorcaminho.produtividade.notetest.databinding.ActivityMainBinding
import com.seumelhorcaminho.produtividade.notetest.repository.NoteRepository
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModel
import com.seumelhorcaminho.produtividade.notetest.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Config toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar o NavController com o NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar a ActionBar com o NavController e AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupViewModel()
    }

    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory)[NoteViewModel::class.java]

    }

    override fun onSupportNavigateUp(): Boolean {
//        return if (navController.currentDestination?.id == R.id.listNoteFragment) {
//            finish()
//            true
//        } else {
            return navController.navigateUp() || super.onSupportNavigateUp()
//        }
    }
}
