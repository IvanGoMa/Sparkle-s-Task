package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Home : AppCompatActivity() {
    lateinit var cvCalendari: CalendarView
    lateinit var back : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home)
        initComponents()
        initListeners()
        initUI()
    }



    fun initListeners(){

        back.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }
    fun initComponents(){
        cvCalendari = findViewById<CalendarView>(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000
        back = findViewById(R.id.back)


    }
    fun initUI(){}

    


}