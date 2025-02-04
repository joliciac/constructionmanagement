package com.example.constructionmanagement.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

private val LANGUAGE_KEY = stringPreferencesKey("selected_language")

suspend fun saveLanguage(context: Context, languageCode: String){
    context.dataStore.edit { preferences ->
        preferences[LANGUAGE_KEY] = languageCode
    }
}

fun getSelectedLanguage(context: Context): Flow<String?> {
    return context.dataStore.data.map {preferences ->
        preferences[LANGUAGE_KEY]
    }
}