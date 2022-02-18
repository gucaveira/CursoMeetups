package com.gustavo.cursomeetups.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gustavo.cursomeetups.databinding.InscricaoItemBinding
import com.gustavo.cursomeetups.model.Evento

class ListaInscricoesAdapter(
    private val inscricoes: MutableList<Evento> = mutableListOf(),
    val cliqueNoItem: (id: String) -> Unit,
) : RecyclerView.Adapter<ListaInscricoesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            InscricaoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(inscricoes[position])
    }

    override fun getItemCount(): Int = inscricoes.size

    fun atualiza(inscricoes: List<Evento>) {
        this.inscricoes.clear()
        this.inscricoes.addAll(inscricoes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: InscricaoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var evento: Evento

        init {
            itemView.setOnClickListener {
                if (::evento.isInitialized) {
                    cliqueNoItem(this.evento.id)
                }
            }
        }

        fun vincula(evento: Evento) {
            this.evento = evento
            binding.inscricaoItemTitulo.text = this.evento.titulo
            binding.inscricaoItemImagem.load(this.evento.imagem)

            if (this.evento.imagem.isNullOrBlank()) {
                binding.inscricaoItemImagem.visibility = View.GONE
            } else {
                binding.inscricaoItemImagem.visibility = View.VISIBLE
            }
        }
    }
}
