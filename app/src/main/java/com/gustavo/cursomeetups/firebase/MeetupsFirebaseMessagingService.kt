package com.gustavo.cursomeetups.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.gustavo.cursomeetups.model.Dispositivo
import com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences
import com.gustavo.cursomeetups.repository.DispositivoRepository
import org.koin.android.ext.android.inject

class MeetupsFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MeetupsFCM"
    }

    private val dispositivoRepository: DispositivoRepository by inject()
    private val preferences: FirebaseTokenPreferences by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.i(TAG, "onNewTokn: $token")
        preferences.tokenNovo()
        dispositivoRepository.salva(Dispositivo(token = token))
    }
}