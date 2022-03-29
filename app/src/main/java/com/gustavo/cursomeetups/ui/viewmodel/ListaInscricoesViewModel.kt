package com.gustavo.cursomeetups.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.repository.EventoRepository
import kotlinx.coroutines.launch

class ListaInscricoesViewModel(private val repository: EventoRepository) : ViewModel() {

    private val _eventoData = MutableLiveData<List<Evento>>()
    val eventoData: LiveData<List<Evento>> = _eventoData

    private val _throwable = MutableLiveData<String>()
    val throwable: LiveData<String> = _throwable

    fun buscaInscricoes() {
        viewModelScope.launch {
            repository.buscaInscricoes().fold(
                onSuccess = {
                    _eventoData.value = it
                    it.forEach { evento ->
                        FirebaseMessaging.getInstance().subscribeToTopic(evento.id)
                    }

                },
                onFailure = { throwable ->
                    _throwable.value = throwable.message
                    Log.e("ListaEventosViewModel", throwable.toString())
                }
            )
        }
    }
}
