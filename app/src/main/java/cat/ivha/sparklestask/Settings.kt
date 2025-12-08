package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Settings: AppCompatActivity() {
    lateinit var btnGuarda : Button
    lateinit var btnSortir : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnGuarda = findViewById(R.id.btnGuarda)
        btnSortir = findViewById(R.id.btnSortir)
    }

    private fun initListeners() {
        btnGuarda.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        btnSortir.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }
    }
}