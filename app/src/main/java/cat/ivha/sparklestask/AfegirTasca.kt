package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AfegirTasca : AppCompatActivity() {

    lateinit var btnCancel: Button
    lateinit var btnCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_afegir_tasca)
        initComponents()
        initListeners()

    }

    private fun initListeners() {
        btnCancel.setOnClickListener {
            val intent = Intent(this,HomeFragment::class.java)
            startActivity(intent)
        }
        btnCreate.setOnClickListener {
            val intent = Intent(this,HomeFragment::class.java)
            startActivity(intent)
        }

    }

    private fun initComponents() {
        btnCancel = findViewById(R.id.btnCancel)
        btnCreate = findViewById(R.id.btnCreate)
    }
}