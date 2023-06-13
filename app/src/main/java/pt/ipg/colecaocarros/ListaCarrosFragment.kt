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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ipg.colecaocarros.databinding.FragmentListaCarrosBinding

private const val ID_LOADER_LIVROS = 0

class ListaCarrosFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentListaCarrosBinding? = null
    private val binding get() = _binding!!
    var carroselecionado:Carros?=null
        set(value) {
            field = value
            val mostarEliminarEditar = (value != null)
            val activity = activity as MainActivity
            activity.mostraBotaoMenu(R.id.action_editar,mostarEliminarEditar)
            activity.mostraBotaoMenu(R.id.action_eliminar,mostarEliminarEditar)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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



    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        adapterCarros!!.cursor = data
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (adapterCarros != null) {
            adapterCarros!!.cursor = null
        }
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
        val acao = ListaCarrosFragmentDirections.actionListaCarrosFragmentToEliminarCarroFragment(carroselecionado!!)
        findNavController().navigate(acao)
    }

    private fun editarCarro() {
        val acao = ListaCarrosFragmentDirections.actionListaCarrosFragmentToNovoCarroFragment(carroselecionado!!)
        findNavController().navigate(acao)
    }

    private fun adicionaCarro() {
        val acao = ListaCarrosFragmentDirections.actionListaCarrosFragmentToNovoCarroFragment(null)
        findNavController().navigate(acao)
    }
}