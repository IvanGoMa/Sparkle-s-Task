package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cat.ivha.sparklestask.databinding.RegisterBinding


class Register : AppCompatActivity() {
    private val viewmodel: RegisterViewModel by viewModels()
    private lateinit var binding : RegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        val btnBack = binding.btnBack
        val btnInici = binding.btnInici
        val etEmail = binding.etEmail
        val etUser = binding.etUser
    }

    private fun initListeners() {
        btnBack.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }

        btnInici.setOnClickListener {
            val intent = Intent(this, MenuBottom::class.java)
            startActivity(intent)
        }
    }
}