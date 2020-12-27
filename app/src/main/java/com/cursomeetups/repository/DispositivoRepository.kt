package com.cursomeetups.repository

import android.util.Log
import com.cursomeetups.model.Dispositivo
import com.cursomeetups.webclient.DispositivoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Dispositivo"

class DispositivoRepository(private val service: DispositivoService) {

    fun salva(dispositivo: Dispositivo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.salva(dispositivo)
                if (response.isSuccessful) {
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