package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserMail ( private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserMail")
        val USER_MAIL = stringPreferencesKey ( "user_mail" )
        val DARK_MODE = booleanPreferencesKey ( "dark_mode" )
    }

    // Flow para correo
    val getMail: Flow<String> = context.dataStore.data
        .map {
            preference ->
            preference[USER_MAIL] ?: ""
        }

    // Flow para dark mode
    val getDarkMode: Flow<Boolean> = context.dataStore.data
        .map {
            preference ->
            preference[DARK_MODE] ?: false
        }

    // Guardar correo
    suspend fun saveMail ( mail:String ) {
        context.dataStore.edit {
            preference ->
            preference [USER_MAIL] = mail
        }
    }

    // Guardar dark mode
    suspend fun saveDarkMode ( isDarkMode: Boolean ) {
        context.dataStore.edit {
            preference ->
            preference [DARK_MODE] = isDarkMode
        }
    }
}