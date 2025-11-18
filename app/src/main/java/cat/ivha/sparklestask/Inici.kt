package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java


class Inici : AppCompatActivity() {

    private val password = "1234"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inici)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPassw = findViewById<EditText>(R.id.etPssw)
        val btnInici = findViewById<Button>(R.id.btnInici)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnInici.setOnClickListener {

            val user = etUser.text.toString()
            val pass = etPassw.text.toString()

            if (user.isBlank() || pass.isBlank()){
                Toast.makeText(this, "Completa tots els camps. ", Toast.LENGTH_SHORT).show()
            } else if (pass == password){
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this, "Contrasenya no és correcta", Toast.LENGTH_SHORT).show()
            }

        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    fun navegarInicio(usuario: String){
        val intent = Intent(this, Inici::class.java)
        intent.put
    }
}