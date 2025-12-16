package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PerfilHelp : AppCompatActivity() {

    lateinit var ivSortir : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.perfil_help)
        initComponents()
        initListeners()

    }

    private fun initListeners() {
        ivSortir.setOnClickListener {
            val intent = Intent(this, PerfilFragment::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        ivSortir = findViewById(R.id.ivSortir)
    }
}