package com.cursomeetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cursomeetups.repository.EventoRepository

class DetalhesEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaEvento(id: String) = repository.buscaEvento(id)

    fun inscreve(eventoId: String) = repository.inscreve(eventoId)

    fun cancela(eventoId: String) = repository.cancela(eventoId)
}
