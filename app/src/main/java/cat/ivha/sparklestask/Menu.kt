package cat.ivha.sparklestask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu: AppCompatActivity() {
    lateinit var btnHome: Button
    lateinit var btnRegister: Button
    lateinit var btnLogin: Button
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
        btnRegister=findViewById(R.id.btnRegister)
        btnLogin=findViewById(R.id.btnLogin)
        btnPerfil=findViewById(R.id. btnPerfil)
        btnAjustos=findViewById(R.id.btnAjustos)

    }

    private fun initListeners() {
        btnHome.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }
        /*
        btnPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
        */

        btnAjustos.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }

}