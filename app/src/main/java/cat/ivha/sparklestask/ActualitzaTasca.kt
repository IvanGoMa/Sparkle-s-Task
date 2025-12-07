package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActualitzaTasca : AppCompatActivity() {

    lateinit var btnCancel: Button
    lateinit var btnSave: Button
    lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualitza_tasca)
        initComponents()
        initListeners()
    }

    private fun initListeners() {
        btnCancel.setOnClickListener {
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
        btnSave.setOnClickListener {
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
        btnDelete.setOnClickListener {
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)
    }
}