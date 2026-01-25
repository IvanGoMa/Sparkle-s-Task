package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import cat.ivha.sparklestask.databinding.IniciBinding

class Inici : AppCompatActivity() {

    private lateinit var binding: IniciBinding

    private val viewModel: IniciViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Pink_SparklesTask)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = IniciBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        observeViewModel()
    }

    private fun initListeners() {
        binding.etUser.addTextChangedListener { text ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.etPssw.addTextChangedListener { text ->
            viewModel.onPasswChanged(text.toString())
        }

        binding.btnInici.setOnClickListener {
            viewModel.onLoginClick()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        // ✅ Observar errores del email
        viewModel.emailError.observe(this) { error ->
            binding.emailInputLayout.error = error
        }

        viewModel.passwError.observe(this) { error ->
            binding.passwInputLayout.error = error
        }

        viewModel.isLoginButtonOn.observe(this) { isEnabled ->
            binding.btnInici.isEnabled = isEnabled
        }

        viewModel.loginSuccesEvent.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Inici de sessió correcte!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MenuBottom::class.java)
                startActivity(intent)
                finish()
                viewModel.onLoginSuccessEventHandled()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}