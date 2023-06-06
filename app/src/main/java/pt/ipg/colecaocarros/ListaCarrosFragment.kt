package pt.ipg.colecaocarros

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipg.colecaocarros.databinding.FragmentListaCarrosBinding

private const val ID_LOADER_LIVROS = 0

class ListaCarrosFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentListaCarrosBinding? = null
    private val binding get() = _binding!!
    var carroselecionado:Carros?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListaCarrosBinding.inflate(inflater,container,false)
        return binding.root
    }

    private var adapterCarros: AdapterCarros? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterCarros = AdapterCarros(this)
        binding.recycleViewCarros.adapter = adapterCarros
        binding.recycleViewCarros.layoutManager = LinearLayoutManager(requireContext())

        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_LIVROS,null,this)

        val activity =activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_lista_carros
    }

    companion object {

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            CarrosContentProvider.ENDERECO_CARROS,
            TabelaCarro.CAMPOS,
            null,
            null,
            TabelaCarro.CAMPO_MARCA
        )
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterCarros!!.cursor = null
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterCarros!!.cursor = data
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_adicionar -> {
                adicionaCarro()
                true
            }
            R.id.action_editar -> {
                editarCarro()
                true
            }
            R.id.action_eliminar -> {
                eliminarCarro()
                true
            }
            else -> false
        }
    }

    private fun eliminarCarro() {
        TODO("Not yet implemented")
    }

    private fun editarCarro() {
        TODO("Not yet implemented")
    }

    private fun adicionaCarro() {
        TODO("Not yet implemented")
    }
}