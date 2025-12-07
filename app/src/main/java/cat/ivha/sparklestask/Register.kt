package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var btnBack : Button
class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnBack = findViewById(R.id.btnBack)
    }

    private fun initListeners() {
        btnBack.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }
    }
}