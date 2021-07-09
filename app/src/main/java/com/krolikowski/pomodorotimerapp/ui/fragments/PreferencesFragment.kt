package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_preferences.*

class PreferencesFragment: Fragment(R.layout.fragment_preferences) {

    private lateinit var preferenceViewModel: QuickPomodoroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quick_pomodoro_settings_save_button.setOnClickListener {
            val quickPomodoroTime = quick_pomodoro_settings_time.text.toString()
            preferenceViewModel.saveToDataStore(quickPomodoroTime)
        }

    }

}