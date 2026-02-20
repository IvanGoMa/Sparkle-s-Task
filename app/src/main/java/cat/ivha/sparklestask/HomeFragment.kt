package cat.ivha.sparklestask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.ivha.sparklestask.databinding.HomeRvBinding
import kotlin.getValue

class HomeFragment : Fragment() {

    // --- VIEW BINDING ---
    private var _binding: HomeRvBinding? = null
    private val binding get() = _binding!!

    // --- VIEWMODEL ---
    private val viewModel: HomeViewModel by activityViewModels()

    // --- ADAPTER ---
    private lateinit var adapter: TasksAdapter


    // --- CICLE DE VIDA ---

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCalendar()
        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // --- CONFIGURACIÓ ---

    private fun setupRecyclerView() {
        adapter = TasksAdapter(
            itemsComplets = emptyList(),
            onItemClick = { task ->
                ActualitzaTasca.newInstance(task).show(childFragmentManager, "Modificar Tasca")
            }
        )
        binding.rvTasques.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasques.adapter = adapter
    }

    private fun setupCalendar() {
        binding.cvCalendari.minDate = System.currentTimeMillis()

        binding.cvCalendari.maxDate = System.currentTimeMillis() + 120L * 24 * 60 * 60 * 1000
    }

    fun actualizarTarea(updatedTask: Task) {
        viewModel.updateTask(updatedTask)
        Toast.makeText(context, "Tasca actualitzada", Toast.LENGTH_SHORT).show()
    }

    fun eliminarTarea(taskId: Long) {
        viewModel.deleteTaska(taskId)
        Toast.makeText(context, "Tasca eliminada", Toast.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
        binding.btnAfegir.setOnClickListener {
            CreateTask().show(parentFragmentManager, "Crear Taska")
        }

        binding.cvCalendari.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dataSeleccionada = dateOf(year, month + 1, dayOfMonth)

            viewModel.filtraTaskaPerData(dataSeleccionada)
        }
    }

    private fun observeViewModel() {
        viewModel.filteredTasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }
    }
}