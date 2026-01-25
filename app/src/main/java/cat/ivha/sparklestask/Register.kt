package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cat.ivha.sparklestask.databinding.RegisterBinding
import kotlin.toString


class Register : AppCompatActivity() {
    // Creem una instància de RegisterViewModel
    private val viewmodel: RegisterViewModel by viewModels()
    // Creem una variable amb el binding d'aquesta Activity
    private lateinit var binding : RegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflem el layaout
        binding = RegisterBinding.inflate(layoutInflater)
        // Utilitzem el binding per fer el setContentView()
        setContentView(binding.root)

        // Inicialtitzem variables amb el binding
        val btnBack = binding.btnBack
        val btnInici = binding.btnInici
        val etEmail = binding.etEmail
        val etUser = binding.etUser
        val etPssw = binding.etPssw
        val etPsswX = binding.etPsswX
        val etData = binding.etHB

        // Listeners
        btnBack.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }

        btnInici.setOnClickListener {

            // Passem els valors dels EditText al ViewModel
            viewmodel.getData(etUser.text.toString(),
                etEmail.text.toString(),
                etPssw.text.toString(),
                etPsswX.text.toString(),
                etData.text.toString())

            // Utilitzem les funcions del ViewModel per fer les comprovacions
            if (!viewmodel.checkUsername()){
                Toast.makeText(this,"El nom d'usuari ha de tenir més de 5 caràcters", Toast.LENGTH_SHORT).show()
            }

            else if (!viewmodel.checkPassword()){
                Toast.makeText(this,"La contrasenya ha de contenir minúscules, majúscules, nombres, símbols i tenir més de 7 caràcters", Toast.LENGTH_SHORT).show()
            }

            else if (!viewmodel.checkPasswordValidation()){
                Toast.makeText(this,"La contrasenya no és igual als dos camps", Toast.LENGTH_SHORT).show()
            }

            else if (!viewmodel.checkEmail()){
                Toast.makeText(this,"Email invàlid", Toast.LENGTH_SHORT).show()
            }

            else if (!viewmodel.checkDate()){
                Toast.makeText(this,"Has de ser major d'edat", Toast.LENGTH_SHORT).show()
            }

            else{
                val intent = Intent(this, MenuBottom::class.java)
                startActivity(intent)
            }
        }
    }


}