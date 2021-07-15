package com.krolikowski.pomodorotimerapp.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.MainActivity
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_preferences.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

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
            var applicationLanguage = pomodoro_application_language.text.toString()

            if (applicationLanguage == "Polski"){
                applicationLanguage = "pl"
            } else {
                applicationLanguage = "en"
            }

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

            val lang: String

            if (language == "pl"){
                lang = "Polski"
            } else{
                lang = "English"
            }

            pomodoro_application_language.setText(lang, false)
        })
    }

}