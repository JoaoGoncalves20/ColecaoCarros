package pt.ipg.colecaocarros

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AdapterCarros(val fragment: ListaCarrosFragment) : RecyclerView.Adapter<AdapterCarros.ViewHolderCarros>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolderCarros(contentor: View) : ViewHolder(contentor) {
        private val textViewMarca = contentor.findViewById<TextView>(R.id.textViewMarca)
        private val textViewModelo = contentor.findViewById<TextView>(R.id.textViewModelo)

        init {
            contentor.setOnClickListener {
                viewHolderSeleccionado?.desSeleciona()
                seleciona()
            }
        }

        internal var carros: Carros? = null
            set(value) {
                field = value
                textViewMarca.text = carros?.marca?: ""
                textViewModelo.text = carros?.detalhes.toString()?: ""
            }

            fun seleciona() {
                viewHolderSeleccionado = this
                itemView.setBackgroundResource(R.color.item_selecionado)
            }

            fun desSeleciona() {
                itemView.setBackgroundResource(android.R.color.white)
            }
    }

        private var viewHolderSeleccionado : ViewHolderCarros? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarros {
        return ViewHolderCarros(
        fragment.layoutInflater.inflate(R.layout.item_carro, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {
        cursor!!.moveToPosition(position)
        holder.carros = Carros.fromCursor(cursor!!)
    }
}