package com.example.forecastmvvm.data.provider

import android.content.Context
import com.example.forecastmvvm.internal.LanguageSystem

const val LANGUAGE_SYSTEM = "LANGUAGE_SYSTEM"

class LanguageProviderImpl(context: Context) : PreferenceProvider(context), LanguageProvider {

    override fun getLanguageSystem(): LanguageSystem {
        val selectedName = preferences.getString(LANGUAGE_SYSTEM, LanguageSystem.ENGLISH.name)
        return LanguageSystem.valueOf(selectedName!!)
    }
}