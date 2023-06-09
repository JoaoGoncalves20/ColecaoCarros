package pt.ipg.colecaocarros

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import pt.ipg.colecaocarros.databinding.FragmentNovoCarroBinding
import pt.ipg.colecaocarros.databinding.FragmentSobreBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


private const val ID_LOADER_DETALHES = 0

class NovoCarroFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var _binding: FragmentNovoCarroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNovoCarroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_DETALHES,null,this)

        val activity =activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_guardar -> {
                guardar()
                true
            }
            R.id.action_cancelar -> {
                voltaListaCarros()
                true
            }
            else -> false
        }
    }

    private fun voltaListaCarros() {
        findNavController().navigate((R.id.action_ListaCarrosFragment_to_novoCarroFragment))
    }

    private fun guardar() {
        val marca = binding.TextInputMarca.text.toString()
        if(marca.isBlank()){
            binding.TextInputMarca.error= getString(R.string.campo_vazio)
            binding.TextInputMarca.requestFocus()
            return
        }

        val modelo = binding.TextInputModelo.text.toString()
        if(modelo.isBlank()){
            binding.TextInputModelo.error = getString(R.string.campo_vazio)
            binding.TextInputModelo.requestFocus()
            return
        }

        val data: Date

        val df = SimpleDateFormat("dd-MM-yyyy")
        try {
            data = df.parse(binding.TextInputData.text.toString())
        } catch (e: Exception) {
            binding.TextInputData.error = getString(R.string.data_invalida)
            binding.TextInputData.requestFocus()
            return
        }

        val detalhes = binding.spinnerDetalhes.selectedItemId

        val calendario = Calendar.getInstance()

        calendario.time = data

        val carro = Carros(
            marca,
            modelo,
            calendario,
            Detalhes("?", 0.0,0.0,detalhes)

        )

        requireActivity().contentResolver.insert(
            CarrosContentProvider.ENDERECO_CARROS,
            carro.toContentValues()
        )

        if(id == null){
            binding.TextInputMarca.error = getString(R.string.erro_guardar_carro)
            return
        }

        Toast.makeText(requireContext(),getString(R.string.guardar_carro_sucesso), Toast.LENGTH_SHORT)
        return
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            CarrosContentProvider.ENDERECO_CARROS,
            TabelaDetalhes.CAMPOS,
            null,
            null,
            TabelaDetalhes.CAMPO_ESTADO
        )
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        binding.spinnerDetalhes.adapter = null
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null){
            binding.spinnerDetalhes.adapter = null
            return
        }
    binding.spinnerDetalhes.adapter = SimpleCursorAdapter(
        requireContext(),
        android.R.layout.simple_list_item_1,
        data,
        arrayOf(TabelaDetalhes.CAMPO_ESTADO),
        intArrayOf(android.R.id.text1),
        0
    )
    }
}