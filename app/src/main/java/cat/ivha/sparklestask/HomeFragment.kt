package cat.ivha.sparklestask

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.home) {
    lateinit var cvCalendari: CalendarView

    override fun onViewCreated(view: View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
        initListeners()
        initUI()
    }

    fun initListeners(){}
    fun initComponents(view: View){
        cvCalendari = view.findViewById(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000


    }
    fun initUI(){}


}