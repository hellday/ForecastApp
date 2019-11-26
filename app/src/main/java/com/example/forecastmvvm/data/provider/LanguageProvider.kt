package com.example.forecastmvvm.data.provider

import com.example.forecastmvvm.internal.LanguageSystem

interface LanguageProvider {
    fun getLanguageSystem() : LanguageSystem
}