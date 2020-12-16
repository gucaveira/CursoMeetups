package com.cursomeetups.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.cursomeetups.model.Evento
import com.cursomeetups.model.Usuario
import com.cursomeetups.webclient.EventoService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.net.SocketTimeoutException

private const val FALHA = "Falha ao buscar eventos"

class EventoRepository(
    private val service: EventoService
) {

    private val email get() = FirebaseAuth.getInstance().currentUser?.email

    fun buscaTodos(): LiveData<Resultado<List<Evento>>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaService(mensagemDeErro = FALHA) {
                    service.buscaTodos()
                }
            emit(resultado)
        }

    fun buscaEvento(id: String): LiveData<Resultado<Evento>?> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaService(mensagemDeErro = FALHA) {
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    service.buscaPorId(id, email)
                }
            emit(resultado)
        }


    fun inscreve(eventoId: String): LiveData<Resultado<Boolean>> =
        liveData(Dispatchers.IO) {
            val resultado =
                executaServiceSemResultado(mensagemDeErro = FALHA) {
                    service.inscreve(eventoId, Usuario(email))
                }
            emit(resultado)
        }

    fun cancela(evetnId: String): LiveData<Resultado<Boolean>> =
        liveData(Dispatchers.IO) {
            val resultado = executaServiceSemResultado(mensagemDeErro = FALHA) {
                service.cancela(evetnId, Usuario(email))
            }
            emit(resultado)
        }

    fun buscaInscricoes(): LiveData<Resultado<List<Evento>>> =
        liveData(Dispatchers.IO) {
            val resultado = email?.let { email ->
                executaService(mensagemDeErro = FALHA) {
                    service.buscaInscricoes(email)
                }
            } ?: Resultado(erro = "Usuário não autenticado")
            emit(resultado)
        }

    private suspend fun executaServiceSemResultado(
        mensagemDeErro: String,
        executa: suspend () -> Response<Unit>
    ): Resultado<Boolean> {
        return try {
            criaResultadoSemResposta(executa(), mensagemDeErro)
        } catch (e: SocketTimeoutException) {
            Resultado(erro = FALHA)
        } catch (e: java.lang.Exception) {
            Resultado(erro = FALHA)
        }
    }

    private fun <T> criaResultado(
        resposta: Response<T>,
        mensagemDeErro: String? = null
    ): Resultado<T>? {
        if (resposta.isSuccessful) {
            return resposta.body()?.let { Resultado(it) }
        }
        return Resultado(erro = mensagemDeErro)
    }


    private suspend fun <T> executaService(
        mensagemDeErro: String = "Falha na requisição",
        executa: suspend () -> Response<T>,
    ): Resultado<T>? {
        return try {
            criaResultado(executa(), mensagemDeErro)
        } catch (e: SocketTimeoutException) {
            Resultado(erro = "Falha ao conectar com o servidor")
        } catch (e: Exception) {
            Resultado(erro = "Erro desconhecido")
        }
    }

    private fun criaResultadoSemResposta(
        sucesso: Response<Unit>,
        mensagemDeErro: String? = null
    ): Resultado<Boolean> {
        if (sucesso.isSuccessful) {
            return Resultado(true)
        }
        return Resultado(false, mensagemDeErro)
    }

}