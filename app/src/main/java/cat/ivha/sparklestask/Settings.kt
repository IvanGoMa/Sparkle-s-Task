package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Settings: AppCompatActivity() {
    lateinit var back : LinearLayout
    lateinit var btnSortir : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        back = findViewById(R.id.back)
        btnSortir = findViewById(R.id.btnSortir)
    }

    private fun initListeners() {
        back.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        btnSortir.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }
    }
}