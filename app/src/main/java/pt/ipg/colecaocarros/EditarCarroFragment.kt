package pt.ipg.colecaocarros

import android.database.Cursor
import android.net.Uri
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
import pt.ipg.colecaocarros.databinding.FragmentEditarCarroBinding
import java.util.Calendar


private const val ID_LOADER_DETALHES = 0

class EditarCarroFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var carro: Carros?= null
    private var _binding: FragmentEditarCarroBinding? = null
    private var data: Calendar? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditarCarroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarViewData.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            if (data == null) data = Calendar.getInstance()
            data!!.set(year, month, dayOfMonth)
        }

        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_DETALHES,null,this)

        val activity =activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar

        val carro = EditarCarroFragmentArgs.fromBundle(requireArguments()).carro

        if (carro != null) {
            activity.atualizaMarca(R.string.marca_label)

            binding.TextInputMarca.setText(carro.marca)
            binding.TextInputModelo.setText(carro.modelo)
            if (carro.data != null) {
                data = carro.data
                binding.calendarViewData.date = data!!.timeInMillis
            }
        } else {
            activity.atualizaMarca(R.string.nova_marca_label)
        }

        this.carro = carro
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
        findNavController().navigate((R.id.action_novoCarroFragment_to_ListaCarrosFragment))
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

        val detalhes = binding.spinnerDetalhes.selectedItemId

        if (carro == null) {
            val livro = Carros(
                marca,
                modelo,
                data,
                Detalhes("?", detalhes),
            )

            insereCarro(livro)
        } else {
            val carro = carro!!
            carro.marca = marca
            carro.modelo = modelo
            carro.data = data
            carro.detalhes = Detalhes("?", detalhes)

            alteraCarro(carro)
        }
    }

    private fun alteraCarro(carros: Carros) {
        val enderecoCarro = Uri.withAppendedPath(CarrosContentProvider.ENDERECO_CARROS, carros.id.toString())
        val carrosAlterados = requireActivity().contentResolver.update(enderecoCarro, carros.toContentValues(), null, null)

        if (carrosAlterados == 1) {
            Toast.makeText(requireContext(), R.string.carro_guardado_com_sucesso, Toast.LENGTH_LONG).show()
            voltaListaCarros()
        } else {
            binding.TextInputMarca.error = getString(R.string.erro_ao_guardar_carro)
        }
    }

    private fun insereCarro(
        carro: Carros
    ) {
        val id = requireActivity().contentResolver.insert(
            CarrosContentProvider.ENDERECO_CARROS,
            carro.toContentValues()
        )

        if (id == null) {
            binding.TextInputMarca.error = getString(R.string.erro_ao_guardar_carro)
            return
        }

        Toast.makeText(
            requireContext(),
            getString(R.string.carro_guardado_com_sucesso),
            Toast.LENGTH_SHORT
        ).show()

        voltaListaCarros()
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
        mostraDetalheSelecionadaSpinner()
    }
    private fun mostraDetalheSelecionadaSpinner() {
        if (carro == null) return

        val idCategoria = carro!!.detalhes.id

        val ultimaCategoria = binding.spinnerDetalhes.count - 1
        for (i in 0..ultimaCategoria) {
            if (idCategoria == binding.spinnerDetalhes.getItemIdAtPosition(i)) {
                binding.spinnerDetalhes.setSelection(i)
                return
            }
        }
    }
}