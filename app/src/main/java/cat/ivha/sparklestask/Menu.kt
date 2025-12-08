package cat.ivha.sparklestask


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu: AppCompatActivity() {
    lateinit var btnHome: Button

    lateinit var btnPerfil: Button
    lateinit var btnAjustos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.menu)
        super.onCreate(savedInstanceState)

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnHome=findViewById(R.id.btnHome)
        btnPerfil=findViewById(R.id. btnPerfil)
        btnAjustos=findViewById(R.id.btnAjustos)

    }

    private fun initListeners() {
        btnHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }



        btnPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        btnAjustos.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }

}