package cat.ivha.sparklestask

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

class HomeFragment : Fragment(R.layout.home_rv) {

    lateinit var ivHelp : ImageView
    lateinit var cvCalendari: CalendarView
    lateinit var btnAfegir: Button
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TasksAdapter

    val items = TasksList.items


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_rv,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvTasques)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TasksAdapter(
            itemsComplets = items,
            onItemClick = { item ->
            ActualitzaTasca(item).show(parentFragmentManager,"Modificar Tasca")
            }

        )
        recyclerView.adapter=adapter
        initComponents(view)
        initListeners()
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