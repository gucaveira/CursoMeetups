package com.cursomeetups.firebase

import android.util.Log
import com.cursomeetups.model.Dispositivo
import com.cursomeetups.preferences.FirebaseTokenPreferences
import com.cursomeetups.repository.DispositivoRepository
import com.google.firebase.messaging.FirebaseMessagingService
import org.koin.android.ext.android.inject

private const val TAG = "MeetupsFCM"

class MeetupsFirebaseMessagingService : FirebaseMessagingService() {

    private val dispositivoRepository: DispositivoRepository by inject()
    private val preferences: FirebaseTokenPreferences by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewTokn: $token")
        preferences.tokenNovo()
        dispositivoRepository.salva(Dispositivo(token = token))
    }
}