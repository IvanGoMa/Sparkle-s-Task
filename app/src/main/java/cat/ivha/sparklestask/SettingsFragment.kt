package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import cat.ivha.sparklestask.databinding.HomeRvBinding
import cat.ivha.sparklestask.databinding.PerfilRvBinding
import cat.ivha.sparklestask.databinding.SettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = SettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initListeners()
    }

    fun initListeners(){
        binding.btnSortir.setOnClickListener {
            val intent = Intent(requireContext(), Inici::class.java)
            startActivity(intent)
        }
        binding.btnGuarda.setOnClickListener{
            Toast.makeText(
                requireContext(),
                "Preferencies guardades",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {

        viewModel.totalSparks.observe(viewLifecycleOwner) { sparks ->
            val tvSparksTotal = binding.tvSparksTotal
            tvSparksTotal.text = sparks.toString()
        }
    }




}