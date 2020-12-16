package com.cursomeetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cursomeetups.repository.EventoRepository

class ListaInscricoesViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaInscricoes() = repository.buscaInscricoes()
}
