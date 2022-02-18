package com.gustavo.cursomeetups.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.databinding.ListaEventosBinding
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.ui.extensions.snackBar
import com.gustavo.cursomeetups.ui.recyclerview.adapter.ListaEventosAdapter
import com.gustavo.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.gustavo.cursomeetups.ui.viewmodel.ListaEventoViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListaEventosFragment : BaseFragment(R.layout.lista_eventos) {


    private val viemModel: ListaEventoViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val controller: NavController by lazy { findNavController() }
    private val adapter by lazy { configuraAdapter() }


    private var _binding: ListaEventosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ListaEventosBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraSwipe()
        buscaEventos()
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes =
            ComponentesVisuais(appBar = true, bottomNavigation = true)
    }

    private fun configuraSwipe() {
        binding.listaEventosSwipeRefresh.setOnRefreshListener {
            buscaEventos()
        }
    }

    private fun buscaEventos() {
        viemModel.buscaTodos()
        viemModel.eventoData.observe(viewLifecycleOwner) { resultdo ->
            binding.listaEventosSwipeRefresh.isRefreshing = false
            atualizar(resultdo)
        }

        viemModel.throwable.observe(viewLifecycleOwner) {
            view?.snackBar(it)
        }
    }

    private fun atualizar(eventos: List<Evento>) {
        adapter.atualiza(eventos)
    }

    private fun configuraRecyclerView() {
        binding.listaEventosRecyclerview.adapter = adapter
    }

    private fun configuraAdapter(): ListaEventosAdapter {
        return ListaEventosAdapter(cliqueNoItem = { id -> vaiParaDetalhesDoEvento(id) })
    }

    private fun vaiParaDetalhesDoEvento(id: String) {
        ListaEventosFragmentDirections.actionListaEventosToDetalhesEvento(id)
            .let(controller::navigate)
    }
}