package com.gustavo.cursomeetups.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.repository.EventoRepository
import kotlinx.coroutines.launch

class DetalhesEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    val buscaEvento = MutableLiveData<Evento>()
    val cancela = MutableLiveData<Boolean>()
    val inscreve = MutableLiveData<Boolean>()

    fun buscaEvento(id: String) {
        viewModelScope.launch {
            repository.buscaEvento(id).fold(
                onSuccess = {
                    buscaEvento.value = it
                },
                onFailure = {

                }
            )
        }
    }

    fun inscreve(eventoId: String) {
        viewModelScope.launch {
            repository.inscreve(eventoId).fold(
                onSuccess = {
                    inscreve.value = true
                },
                onFailure = {

                })
        }
    }

    fun cancela(eventoId: String) {
        viewModelScope.launch {
            repository.cancela(eventoId).fold(
                onSuccess = {
                    cancela.value = true
                },
                onFailure = {
                    cancela.value = false
                })
        }
    }
}
