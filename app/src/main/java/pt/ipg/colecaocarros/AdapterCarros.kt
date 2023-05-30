package pt.ipg.colecaocarros

import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AdapterCarros(val Fragment: ListaCarrosFragment) : RecyclerView.Adapter<AdapterCarros.ViewHolderCarros>() {
    var cursor: Cursor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolderCarros(itemView: View) : ViewHolder(itemView) {

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
        TODO("Not yet implemented")
    }
}