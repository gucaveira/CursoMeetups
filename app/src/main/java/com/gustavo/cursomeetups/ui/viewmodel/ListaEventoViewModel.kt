package com.gustavo.cursomeetups.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.repository.EventoRepository
import kotlinx.coroutines.launch

class ListaEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    private val _eventoData = MutableLiveData<List<Evento>>()
    val eventoData: LiveData<List<Evento>> = _eventoData

    private val _throwable = MutableLiveData<String>()
    val throwable: LiveData<String> = _throwable


    fun buscaTodos() {
        viewModelScope.launch {
            repository.buscaTodos().fold(
                onSuccess = {
                    _eventoData.value = it
                },
                onFailure = {
                    _throwable.value = it.message
                }
            )
        }
    }
}
