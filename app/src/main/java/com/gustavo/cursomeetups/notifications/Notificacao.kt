package com.gustavo.cursomeetups.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.gustavo.cursomeetups.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Notificacao(val context: Context) {

    val manager: NotificationManager by lazy {
        context.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        var id: Int = 0
    }

    fun mostrar(data: Map<String, String?>) {
        CoroutineScope(Dispatchers.IO).launch {
            val imagem = tentabuscarImagem(data["imagem"])
            val estilo = criaEstilo(imagem, data)
            val notificacao = criaNotificacao(data, estilo)
            manager.notify(id, notificacao)
            id++
        }
    }

    private fun criaEstilo(imagem: Bitmap?, data: Map<String, String?>): NotificationCompat.Style {
        return imagem?.let {
            NotificationCompat.BigPictureStyle().bigPicture(it)
        } ?: NotificationCompat.BigTextStyle().bigText(data["descricao"])
    }

    private fun criaNotificacao(
        data: Map<String, String?>,
        estilo: NotificationCompat.Style,
    ): Notification {
        return NotificationCompat.Builder(context, "principal")
            .setContentTitle(data["titulo"])
            .setContentText(data["descricao"])
            //.setLargeIcon(imagem)
            .setSmallIcon(R.drawable.ic_acao_novo_evento)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setStyle(NotificationCompat.BigTextStyle().bigText(data["descricao"]))
            //.setStyle(NotificationCompat.BigPictureStyle().bigPicture(imagem).bigLargeIcon(null))
            .setStyle(estilo)
            .build()
    }

    private suspend fun tentabuscarImagem(imagem: String?): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(imagem)
            .build()

        return context.imageLoader.execute(request).drawable?.toBitmap()
    }
}
