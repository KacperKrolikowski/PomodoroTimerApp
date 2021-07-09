package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    private lateinit var preferenceViewModel: QuickPomodoroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)
        preferenceViewModel.readFromDataStore.observe(viewLifecycleOwner, { time ->
            val singlePomodoroTime = time
            pomodoro_name_TV.text = time
        })





    }

}