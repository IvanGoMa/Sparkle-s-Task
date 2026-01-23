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


class Register : AppCompatActivity() {
    private val viewmodel: RegisterViewModel by viewModels()
    private lateinit var binding : RegisterBinding
    lateinit var btnBack: ImageButton
    lateinit var btnInici: Button
    lateinit var etEmail: EditText
    lateinit var etUser: EditText
    lateinit var etPssw: EditText
    lateinit var etPsswX: EditText
    lateinit var etData: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        btnBack = binding.btnBack
        btnInici = binding.btnInici
        etEmail = binding.etEmail
        etUser = binding.etUser
        etPssw = binding.etPssw
        etPsswX = binding.etPsswX
        etData = binding.etHB

    }

    private fun initListeners() {
        btnBack.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }

        btnInici.setOnClickListener {
            viewmodel.username. = etUser.text.toString()
            if (viewmodel.checkAll()){
                val intent = Intent(this, MenuBottom::class.java)
                startActivity(intent)
            }

            Toast.makeText(this,"Camps invàlids", Toast.LENGTH_SHORT).show()
        }


    }
}