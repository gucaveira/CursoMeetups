package com.gustavo.cursomeetups.repository

import android.util.Log
import com.gustavo.cursomeetups.model.Dispositivo
import com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences
import com.gustavo.cursomeetups.webclient.DispositivoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DispositivoRepository(
    private val service: DispositivoService,
    private val preferences: FirebaseTokenPreferences,
) {

    companion object {
        private const val TAG = "Dispositivo"
    }

    fun salva(dispositivo: Dispositivo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.salva(dispositivo)
                if (response.isSuccessful) {
                    preferences.tokenEnviado()
                    Log.i(TAG, "Salva: token enviado ${dispositivo.token} ")
                } else {
                    Log.i(TAG, "Salva: Falha ao enviar o token")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Salva: falha ao comunicar com o servidor")
            }
        }
    }
}
