package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import cat.ivha.sparklestask.databinding.RegisterBinding


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

        initListeners()

    }

    private fun initListeners(){

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, Inici::class.java)
            startActivity(intent)
        }

        binding.etUser.addTextChangedListener { text ->
            viewmodel.onUserChanged(text.toString())
        }

        binding.etEmail.addTextChangedListener { text ->
            viewmodel.onEmailChanged(text.toString())
        }

        binding.etPssw.addTextChangedListener { text ->
            viewmodel.onPsswChanged(text.toString())
        }

        binding.etPsswX.addTextChangedListener { text ->
            viewmodel.onPsswXChanged(text.toString())
        }

        binding.etHB.addTextChangedListener { text ->
            viewmodel.onDateChanged(text.toString())
        }



    }

    private fun observeViewmodel(){
        viewmodel.user
    }


}