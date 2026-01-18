package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.home) {

    lateinit var ivHelp : ImageView
    lateinit var cvCalendari: CalendarView
    lateinit var btnAfegir: Button
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TasksAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rec)
        initComponents(view)
        initListeners()
    }

    private fun initListeners() {
        btnAfegir.setOnClickListener {
            CreateTask().show(parentFragmentManager,"Crear Tasca")
        }

        tasca1.setOnClickListener {
            ActualitzaTasca().show(parentFragmentManager, "Actualitza tasca")
        }
    }


    private fun initComponents(view: View) {
        cvCalendari = view.findViewById(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000
        tasca1 = view.findViewById(R.id.cvTasca1)
        tasca2 = view.findViewById(R.id.cvTasca2)
        tasca3 = view.findViewById(R.id.cvTasca3)
        tasca4 = view.findViewById(R.id.cvTasca4)
        tasca5 = view.findViewById(R.id.cvTasca5)
        btnAfegir = view.findViewById(R.id.btnAfegir)
        ivHelp = view.findViewById(R.id.ivHelp)

    }


}