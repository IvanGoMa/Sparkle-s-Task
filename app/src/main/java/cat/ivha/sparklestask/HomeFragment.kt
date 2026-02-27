package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.ivha.sparklestask.databinding.HomeRvBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: HomeRvBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var adapter: TasksAdapter

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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
        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupRecyclerView() {
        adapter = TasksAdapter(
            itemsComplets = emptyList(),
            onItemClick = { task ->
                ActualitzaTasca.newInstance(task).show(childFragmentManager, "Modificar Tasca")
            },
            onBinClick = { id ->
                viewModel.deleteTaska(id)
            },
            onCheckboxClick = { id ->
                viewModel.completeTask(id)
            }
        )
        binding.rvTasques.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasques.adapter = adapter
    }


    private fun setupListeners() {
        binding.btnAfegir.setOnClickListener {
            CreateTask().show(parentFragmentManager, "Crear Taska")
        }

        binding.btnMostrarTodas.setOnClickListener {
            quitarFiltro()
        }

        binding.btnAbrirCalendario.setOnClickListener {
            mostrarDialogCalendario()
        }
    }

    private fun mostrarDialogCalendario() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_calendari)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val calendarView = dialog.findViewById<CalendarView>(R.id.cvCalendariDialog)
        val btnAceptar = dialog.findViewById<Button>(R.id.btnAceptar)
        val btnCancelar = dialog.findViewById<Button>(R.id.btnCancelar)

        calendarView.minDate = System.currentTimeMillis()
        calendarView.maxDate = System.currentTimeMillis() + 120L * 24 * 60 * 60 * 1000

        viewModel.selectedData.value?.let { fechaActual ->
            calendarView.date = fechaActual.time
        }

        var fechaSeleccionadaTemp: Date = viewModel.selectedData.value ?: Date(System.currentTimeMillis())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            fechaSeleccionadaTemp = dateOf(year, month + 1, dayOfMonth)
        }

        btnAceptar.setOnClickListener {
            aplicarFiltroFecha(fechaSeleccionadaTemp)
            dialog.dismiss()
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun aplicarFiltroFecha(fecha: Date) {
        viewModel.filtraTaskaPerData(fecha)

        binding.tvFechaSeleccionada.visibility = View.VISIBLE
        binding.tvFechaSeleccionada.text = "Filtrado por: ${dateFormat.format(fecha)}"

        Toast.makeText(context, "Mostrant tasques del ${dateFormat.format(fecha)}", Toast.LENGTH_SHORT).show()
    }

    private fun quitarFiltro() {
        viewModel.mostraTasques()

        binding.tvFechaSeleccionada.visibility = View.GONE

        Toast.makeText(context, "Mostrant totes les tasques", Toast.LENGTH_SHORT).show()
    }

    fun eliminarTarea(taskId: Long) {
        viewModel.deleteTaska(taskId)
        Toast.makeText(context, "Tasca eliminada", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.filteredTasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }

        viewModel.selectedData.observe(viewLifecycleOwner) { fecha ->
            if (fecha != null) {
                binding.tvFechaSeleccionada.visibility = View.VISIBLE
                binding.tvFechaSeleccionada.text = "Filtrado por: ${dateFormat.format(fecha)}"
            } else {
                binding.tvFechaSeleccionada.visibility = View.GONE
            }
        }
    }
}