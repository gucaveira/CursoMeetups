package com.gustavo.cursomeetups.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class FirebaseTokenPreferences(context: Context) {

    companion object {
        const val path = "com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences"
        private const val CHAVE_ENVIADO = "enviado"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(path,
        Context.MODE_PRIVATE)

    val enviado get() = preferences.getBoolean(CHAVE_ENVIADO, false)

    fun tokenNovo() {
        preferences.edit {
            putBoolean(CHAVE_ENVIADO, false)
        }
    }

    fun tokenEnviado() {
        preferences.edit {
            putBoolean(CHAVE_ENVIADO, true)
        }
    }
}
