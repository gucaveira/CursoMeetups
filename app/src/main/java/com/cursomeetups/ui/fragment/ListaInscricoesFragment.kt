package com.cursomeetups.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.cursomeetups.R
import com.cursomeetups.ui.extensions.snackBar
import com.cursomeetups.ui.recyclerview.adapter.ListaInscricoesAdapter
import com.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.cursomeetups.ui.viewmodel.EstadoAppViewModel
import com.cursomeetups.ui.viewmodel.ListaInscricoesViewModel
import kotlinx.android.synthetic.main.lista_inscricoes.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListaInscricoesFragment : BaseFragment(R.layout.lista_inscricoes) {

    private val viewModel: ListaInscricoesViewModel by viewModel()
    private val controlador by lazy {
        findNavController()
    }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val adapter by lazy {
        ListaInscricoesAdapter(
            requireContext(),
            cliqueNoItem = this::vaiParaDetalhesDoEvento
        )
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
        viewModel.buscaInscricoes().observe(viewLifecycleOwner) {
            it?.let { resultado ->
                resultado.dado?.let(adapter::atualiza)
                resultado.erro?.let { erro ->
                    view?.snackBar(erro)
                }
            }
        }
    }

    private fun vaiParaDetalhesDoEvento(eventoId: String) {
        ListaInscricoesFragmentDirections
            .acaoListaInscricoesParaDetalhesEvento(eventoId)
            .let(controlador::navigate)
    }

    private fun configuraRecyclerView() {
        val recyclerView = lista_inscricoes_recyclerview
        recyclerView.adapter = adapter
        val divisor = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(divisor)
    }
}