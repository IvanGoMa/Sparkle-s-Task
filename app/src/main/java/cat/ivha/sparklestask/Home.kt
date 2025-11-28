package cat.ivha.sparklestask

import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Home : AppCompatActivity() {
    lateinit var cvCalendari: CalendarView

    override fun onCreate(savedInstanceState: Bundle?):{
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home)
        initComponents()
        initListeners()
        initUI()
    }



    fun initListeners(){}
    fun initComponents(){
        cvCalendari = findViewById<CalendarView>(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000


    }
    fun initUI(){}


}