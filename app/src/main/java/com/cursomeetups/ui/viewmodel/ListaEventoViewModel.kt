package com.cursomeetups.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cursomeetups.repository.EventoRepository

class ListaEventoViewModel(private val repository: EventoRepository) : ViewModel() {

    fun buscaTodos() = repository.buscaTodos()
}
