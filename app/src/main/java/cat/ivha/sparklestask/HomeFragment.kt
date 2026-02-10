package cat.ivha.sparklestask

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import cat.ivha.sparklestask.databinding.HomeRvBinding

class HomeFragment : Fragment(R.layout.home_rv) {

    private var _binding: HomeRvBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: HomeViewModel by viewModels()

    private lateinit var adapter: TasksAdapter

    private lateinit var binder: HomeBinding


    lateinit var ivHelp : ImageView
    lateinit var cvCalendari: CalendarView
    lateinit var btnAfegir: Button
    lateinit var recyclerView: RecyclerView

    val items = TasksList.items


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeRvBinding.inflate(inflater,container,false)
        return binding.root // que es vista raiz
    }
    override fun onViewCreated(view: View, savedInstanceState:Bundle?) {
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

    private fun setupRecyclerView(){
        adapter = TasksAdapter(
            itemsComplets = emptyList(),
            onItemClick = { task ->
                ActualitzaTasca(task).show(parentFragmentManager,"Modificar Tasca")
            }
        )
        binding.rvTasques.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasques.adapter = adapter
    }

    private fun setupCalendar(){
        binding.cvCalendari.minDate = System.currentTimeMillis()

        binding.cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60 // pa q
    }

    private fun setupListeners(){
        binding.btnAfegir.setOnClickListener {
            CreateTask().show(parentFragmentManager, "Crear Taska")
        }

        binding.cvCalendari.setOnDateChangeListener { _binding,  year, month, dayOfMonth ->
            val dataSeleccionada = dateOf(year, month +1, dayOfMonth)
            viewmodel.filtraTaskaPerData(dataSeleccionada)
        }
    }




    private fun initListeners() {
        btnAfegir.setOnClickListener {
            CreateTask().show(parentFragmentManager,"Crear Tasca")
        }
        cvCalendari.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = dateOf(year, month + 1, dayOfMonth)
            adapter.filtra(selectedDate)
        }


    }


    private fun initComponents(view: View) {
        cvCalendari = view.findViewById(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000
        btnAfegir = view.findViewById(R.id.btnAfegir)
        ivHelp = view.findViewById(R.id.ivHelp)

    }


}