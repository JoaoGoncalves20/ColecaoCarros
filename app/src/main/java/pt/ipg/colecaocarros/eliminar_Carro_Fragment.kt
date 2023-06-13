package pt.ipg.colecaocarros

import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import pt.ipg.colecaocarros.databinding.FragmentEliminarCarroBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [eliminar_Carro_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class eliminar_Carro_Fragment : Fragment() {
    private lateinit var carros: Carros
    private var _binding: FragmentEliminarCarroBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEliminarCarroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_eliminar

        carros = eliminar_Carro_FragmentArgs.fromBundle(requireArguments()).carro

        binding.textViewMarcaE.text = carros.marca
        binding.textViewModeloE.text = carros.modelo
        binding.textViewDetalhesE.text = carros.detalhes.estado
        if (carros.data != null) {
            binding.textViewDataE.text = DateFormat.format("yyyy-MM-dd", carros.data)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_eliminar -> {
                eliminar()
                true
            }
            R.id.action_cancelar -> {
                voltaListaLivros()
                true
            }
            else -> false
        }
    }

    private fun voltaListaLivros() {
        findNavController().navigate(R.id.action_eliminarCarroFragment_to_ListaCarrosFragment)
    }

    private fun eliminar() {
        val enderecocarro = Uri.withAppendedPath(CarrosContentProvider.ENDERECO_CARROS, carros.id.toString())
        val numCarrosEliminados = requireActivity().contentResolver.delete(enderecocarro, null, null)

        if (numCarrosEliminados == 1) {
            Toast.makeText(requireContext(), getString(R.string.carro_eleminado_sucesso), Toast.LENGTH_LONG).show()
            voltaListaLivros()
        } else {
            Snackbar.make(binding.textViewMarcaE, getString(R.string.carro_erro_eliminar), Snackbar.LENGTH_INDEFINITE)
        }
    }
}