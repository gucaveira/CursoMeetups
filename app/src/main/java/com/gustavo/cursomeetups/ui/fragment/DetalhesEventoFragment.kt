package com.gustavo.cursomeetups.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.gustavo.cursomeetups.R
import com.gustavo.cursomeetups.databinding.DetalhesEventoBinding
import com.gustavo.cursomeetups.model.Evento
import com.gustavo.cursomeetups.ui.extensions.snackBar
import com.gustavo.cursomeetups.ui.viewmodel.ComponentesVisuais
import com.gustavo.cursomeetups.ui.viewmodel.DetalhesEventoViewModel
import com.gustavo.cursomeetups.ui.viewmodel.EstadoAppViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DetalhesEventoFragment : BaseFragment(R.layout.detalhes_evento) {

    private val argumentos by navArgs<DetalhesEventoFragmentArgs>()
    private val eventoId: String by lazy { argumentos.eventoId }
    private val viewModel: DetalhesEventoViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val controlador by lazy { findNavController() }


    private var _binding: DetalhesEventoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = DetalhesEventoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaEvento()
    }

    private fun buscaEvento() {
        viewModel.buscaEvento(eventoId)
        viewModel.buscaEvento.observe(viewLifecycleOwner) { evento ->
            configuraViews(evento)
            preencheCampos(evento)
            configuraBotaoInscrever(evento)
        }
    }

    private fun configuraViews(evento: Evento) {
        if (evento.imagem.isNullOrBlank()) {
            binding.detalhesEventoImagem.visibility = View.GONE
        }
        if (evento.inscritos < 1) {
            binding.detalhesEventoContainerInscritos.visibility = View.GONE
        }
    }

    private fun preencheCampos(evento: Evento) {
        binding.detalhesEventoImagem.load(evento.imagem)
        binding.detalhesEventoInscritos.text = "${evento.inscritos}"
        binding.detalhesEventoTitulo.text = evento.titulo
        binding.detalhesEventoDescricao.text = evento.descricao
    }

    private fun configuraBotaoInscrever(evento: Evento) {
        when (evento.estaInscrito) {
            true -> {
                binding.detalhesEventoBotaoToggle.apply {
                    text = context.getString(R.string.cancelar)
                    setBackgroundColor(ContextCompat.getColor(context,
                        R.color.botaoCancelar))
                    setOnClickListener { cancela() }
                }
            }
            false -> {
                binding.detalhesEventoBotaoToggle.apply {
                    text = context.getString(R.string.inscrever)
                    setBackgroundColor(ContextCompat.getColor(context,
                        R.color.botaoInscrever))
                    setOnClickListener { inscreve() }
                }
            }
        }
    }

    private fun inscreve() {
        viewModel.inscreve(eventoId)
        viewModel.inscreve.observe(viewLifecycleOwner) {
            controlador.popBackStack()
        }
    }

    private fun cancela() {
        viewModel.cancela(eventoId)

        viewModel.cancela.observe(viewLifecycleOwner) {
            if (it) {
                controlador.popBackStack()
            } else {
                view?.snackBar("Falha ao cancelar inscrição")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        estadoAppViewModel.temComponentes = ComponentesVisuais(
            appBar = false,
            bottomNavigation = false
        )
    }
}