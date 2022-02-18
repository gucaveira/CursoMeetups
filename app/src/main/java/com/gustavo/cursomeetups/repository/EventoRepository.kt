package com.gustavo.cursomeetups.repository

import com.google.firebase.auth.FirebaseAuth
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.model.Usuario
import com.gustavo.cursomeetups.webclient.EventoService

class EventoRepository(private val service: EventoService) {

    companion object {
        private const val FALHA = "Falha ao buscar eventos"
        private const val USUARIO_NAO_AUTENTICADO = "Usuário não autenticado"
        private const val FALHA_NA_REQUISICAO = "Falha na requisição"
        private const val FALHA_AO_CONECTAR_SERVIDOR = "FALHA AO CONECTAR COM O SERVIDOR"
        private const val ERRO_DESCONHECIDO = "Erro desconhecido"
    }

    private val email get() = FirebaseAuth.getInstance().currentUser?.email

    suspend fun buscaTodos(): Result<List<Evento>> { //  return suspendCoroutine { continuation ->
        return runCatching {
            service.buscaTodos()

            /*   if (response.isSuccessful) {
                   response.body()?.let {
                       continuation.resumeWith(Result.success(it))
                   } ?: continuation.resumeWith(Result.failure(Throwable(response.errorBody()
                       .toString())))

               } else {
                   continuation.resumeWith(Result.failure(Throwable(response.errorBody()
                       .toString())))
               }*/
        }
    }

    suspend fun buscaEvento(id: String): Result<Evento> {
        return runCatching {
            service.buscaPorId(id, email)
        }
    }

    suspend fun inscreve(eventoId: String): Result<Unit> {
        return runCatching {
            service.inscreve(eventoId, Usuario(email))
        }
    }

    suspend fun cancela(eventoId: String): Result<Unit> {
        return runCatching { service.cancela(eventoId, Usuario(email)) }
    }

    suspend fun buscaInscricoes(): Result<List<Evento>> {
        return runCatching {
            service.buscaInscricoes(email)
        }
    }
}
