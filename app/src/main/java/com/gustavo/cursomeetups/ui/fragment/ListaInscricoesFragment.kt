package com.gustavo.cursomeetups.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.databinding.ListaInscricoesBinding
import com.gustavo.cursomeetups.ui.extensions.snackBar
import com.gustavo.cursomeetups.ui.recyclerview.adapter.ListaInscricoesAdapter
import com.gustavo.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.gustavo.cursomeetups.ui.viewmodel.ListaInscricoesViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListaInscricoesFragment : BaseFragment(R.layout.lista_inscricoes) {

    private val viewModel: ListaInscricoesViewModel by viewModel()
    private val controlador by lazy { findNavController() }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val adapter by lazy {
        ListaInscricoesAdapter(cliqueNoItem = this::vaiParaDetalhesDoEvento)
    }

    private var _binding: ListaInscricoesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ListaInscricoesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        buscaInscricoes()
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes = ComponentesVisuais(
            appBar = true,
            bottomNavigation = true
        )
    }

    private fun buscaInscricoes() {
        viewModel.buscaInscricoes()
        viewModel.eventoData.observe(viewLifecycleOwner) {
            adapter.atualiza(it)
        }

        viewModel.throwable.observe(viewLifecycleOwner) {
            view?.snackBar(it)
        }
    }

    private fun vaiParaDetalhesDoEvento(eventoId: String) {
        ListaInscricoesFragmentDirections
            .acaoListaInscricoesParaDetalhesEvento(eventoId)
            .let(controlador::navigate)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.listaInscricoesRecyclerview
        recyclerView.adapter = adapter
        val divisor = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(divisor)
    }
}