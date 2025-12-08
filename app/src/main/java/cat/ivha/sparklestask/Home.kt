package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Home : AppCompatActivity() {

    lateinit var ivHelp : ImageView
    lateinit var cvCalendari: CalendarView
    lateinit var back : LinearLayout

    lateinit var tasca1 : CardView
    lateinit var tasca2 : CardView
    lateinit var tasca3 : CardView
    lateinit var tasca4 : CardView
    lateinit var tasca5 : CardView

    lateinit var btnAfegir: Button

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

        btnAfegir.setOnClickListener {
            val intent = Intent(this, AfegirTasca::class.java)
            startActivity(intent)
        }

        tasca1.setOnClickListener{
            val intent = Intent(this, ActualitzaTasca::class.java)
            startActivity(intent)
        }

        tasca2.setOnClickListener{
            val intent = Intent(this, ActualitzaTasca::class.java)
            startActivity(intent)
        }

        tasca3.setOnClickListener{
            val intent = Intent(this, ActualitzaTasca::class.java)
            startActivity(intent)
        }

        tasca4.setOnClickListener{
            val intent = Intent(this, ActualitzaTasca::class.java)
            startActivity(intent)
        }

        tasca5.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        ivHelp.setOnClickListener {
            val intent = Intent(this, HomeHelp::class.java)
            startActivity(intent)
        }
    }
    fun initComponents(){
        cvCalendari = findViewById(R.id.cvCalendari)
        cvCalendari.minDate = System.currentTimeMillis()
        cvCalendari.maxDate = System.currentTimeMillis() + 14*24*60*60*1000
        back = findViewById(R.id.back)
        tasca1 = findViewById(R.id.cvTasca1)
        tasca2 = findViewById(R.id.cvTasca2)
        tasca3 = findViewById(R.id.cvTasca3)
        tasca4 = findViewById(R.id.cvTasca4)
        tasca5 = findViewById(R.id.cvTasca5)
        btnAfegir = findViewById(R.id.btnAfegir)
        ivHelp = findViewById(R.id.ivHelp)



    }
    fun initUI(){}

    


}