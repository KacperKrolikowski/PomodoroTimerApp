package com.krolikowski.pomodorotimerapp.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.fragments.QuickPomodoroFragment
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceViewModel: QuickPomodoroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)

        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.id){
                    R.id.quickPomodoroFragment, R.id.pomodoroListFragment, R.id.preferencesFragment ->{
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
    }

}