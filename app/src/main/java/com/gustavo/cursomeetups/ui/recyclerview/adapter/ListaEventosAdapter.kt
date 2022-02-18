package com.gustavo.cursomeetups.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gustavo.cursomeetups.databinding.EventoItemBinding
import com.gustavo.cursomeetups.model.Evento

class ListaEventosAdapter(
    private val eventos: MutableList<Evento> = mutableListOf(),
    val cliqueNoItem: (id: String) -> Unit,
) : RecyclerView.Adapter<ListaEventosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(eventos[position])
    }

    override fun getItemCount() = eventos.size

    fun atualiza(eventos: List<Evento>) {
        this.eventos.clear()
        this.eventos.addAll(eventos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: EventoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var evento: Evento

        init {
            itemView.setOnClickListener {
                if (::evento.isInitialized) {
                    cliqueNoItem(evento.id)
                }
            }
        }

        fun vincula(evento: Evento) {
            this.evento = evento
            configuraImagem()
            configuraInscritos()
            preencheCampos()
        }

        private fun configuraInscritos() {
            if (this.evento.inscritos > 0) {
                binding.eventoItemContainerInscritos.visibility = View.VISIBLE
            } else {
                binding.eventoItemContainerInscritos.visibility = View.GONE
            }
        }

        private fun configuraImagem() {
            if (this.evento.imagem.isNullOrBlank()) {
                binding.eventoItemImagem.visibility = View.GONE
            } else {
                binding.eventoItemImagem.visibility = View.VISIBLE
            }
        }

        private fun preencheCampos() {
            binding.eventoItemTitulo.text = this.evento.titulo
            binding.eventoItemDescricao.text = this.evento.descricao
            binding.eventoItemImagem.load(this.evento.imagem)
            binding.eventoItemInscritos.text = "${evento.inscritos}"
        }
    }
}
