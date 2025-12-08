package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
class Perfil : AppCompatActivity() {

    lateinit var back : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.perfil)
        initComponents()
        initListeners()


    }

    private fun initComponents() {
        back = findViewById(R.id.back)
    }

    private fun initListeners() {
        back.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }
}