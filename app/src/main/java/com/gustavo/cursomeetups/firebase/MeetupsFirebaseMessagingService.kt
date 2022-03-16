package com.gustavo.cursomeetups.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.model.Dispositivo
import com.gustavo.cursomeetups.preferences.FirebaseTokenPreferences
import com.gustavo.cursomeetups.repository.DispositivoRepository
import org.koin.android.ext.android.inject

class MeetupsFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MeetupsFCM"
        private const val IDENTIFICADOR_DO_CANAL = "principal"
    }

    private val dispositivoRepository: DispositivoRepository by inject()
    private val preferences: FirebaseTokenPreferences by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.i(TAG, "onNewTokn: $token")
        preferences.tokenNovo()
        dispositivoRepository.salva(Dispositivo(token = token))
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG,
            "onMessageReceived : Recebeu mensagem de notificacao ${remoteMessage.notification}")
        Log.i(TAG, "onMessageReceived : Recebeu mensagem de dado ${remoteMessage.data}")

        val gerenciadorNoticicao = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val data = remoteMessage.data

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nome = getString(R.string.channel_name)
            val descricao = getString(R.string.channel_description)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT

            val canal = NotificationChannel(IDENTIFICADOR_DO_CANAL, nome, importancia)
            canal.description = descricao

            gerenciadorNoticicao.createNotificationChannel(canal)
        }

        val notificacao = NotificationCompat.Builder(this, "principal")
            .setContentTitle(data["titulo"])
            .setContentText(data["descricao"])
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        gerenciadorNoticicao.notify(1, notificacao)
    }
}