package pt.ipg.colecaocarros

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AdapterCarros(val Fragment: ListaCarrosFragment) : RecyclerView.Adapter<AdapterCarros.ViewHolderCarros>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolderCarros(contentor: View) : ViewHolder(contentor) {
        private val textViewMarca = contentor.findViewById<TextView>(R.id.textViewMarca)
        private val textViewModelo = contentor.findViewById<TextView>(R.id.textViewModelo)

        internal var carros: Carros? = null
            set(value) {
                field = value
                textViewMarca.text = carros?.marca?: ""
                textViewMarca.text = carros?.id_detalhes.toString()?: ""
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarros {
        return ViewHolderCarros(
        Fragment.layoutInflater.inflate(R.layout.item_carro, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolderCarros, position: Int) {
        cursor!!.move(position)
        holder.carros = Carros.fromCursor(cursor!!)
    }
}