package com.cursomeetups.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val CHAVE_ENVIADO = "enviado"

class FirebaseTokenPreferences(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "com.cursomeetups.preferences.FirebaseTokenPreferences",
        Context.MODE_PRIVATE
    )
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
