package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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

    override fun onResume() {


        val applicationLanguage = resources.getStringArray(R.array.application_languages_list)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, applicationLanguage)
        pomodoro_application_language.setAdapter(adapter)

        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPreviousPreferences()

        quick_pomodoro_settings_save_button.setOnClickListener {

            if (quick_pomodoro_settings_time.text.isNullOrEmpty() or quick_pomodoro_settings_quantity.text.isNullOrEmpty()) return@setOnClickListener

            val quickPomodoroTime = quick_pomodoro_settings_time.text.toString()
            val quickPomodoroQuantity = quick_pomodoro_settings_quantity.text.toString()
            val applicationLanguage = pomodoro_application_language.text.toString()
            preferenceViewModel.saveToDataStore(quickPomodoroTime, quickPomodoroQuantity, applicationLanguage)
        }

    }

    private fun setPreviousPreferences(){
        preferenceViewModel.readFromDataStoreTime.observe(viewLifecycleOwner, { time ->
            quick_pomodoro_settings_time.setText(time)
            Log.d("DEBUG", time)
        })

        preferenceViewModel.readFromDataStoreQuantity.observe(viewLifecycleOwner, { quantity ->
            quick_pomodoro_settings_quantity.setText(quantity)
            Log.d("DEBUG2", quantity)

        })

        preferenceViewModel.readApplicationLanguage.observe(viewLifecycleOwner, { language ->
            pomodoro_application_language.setText(language, false)
        })
    }

}