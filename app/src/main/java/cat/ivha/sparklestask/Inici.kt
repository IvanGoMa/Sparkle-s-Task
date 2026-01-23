package cat.ivha.sparklestask


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import kotlin.jvm.java




class Inici : AppCompatActivity() {


    private val ViewModel: ViewModelLogin by ViewModel
    private val password = "1234"
    private lateinit var etUser: EditText
    private lateinit var etPassw: EditText
    private lateinit var btnInici: Button
    private lateinit var btnRegister: Button






    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Pink_SparklesTask)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inici)


        initComponents()
        initListeners()
        observeViewModel()


    }


    fun initComponents() {
        etUser = findViewById(R.id.etUser)
        etPassw = findViewById(R.id.etPssw)
        btnInici = findViewById(R.id.btnInici)
        btnRegister = findViewById(R.id.btnRegister)
    }


    fun initListeners() {


        btnInici.setOnClickListener {


            val user = etUser.text.toString()
            val pass = etPassw.text.toString()


            if (user.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Completa tots els camps. ", Toast.LENGTH_SHORT).show()
            } else if (pass == password) {
                val intent = Intent(this, MenuBottom::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Contrasenya no és correcta", Toast.LENGTH_SHORT).show()
            }


        }


        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }


    fun observeViewModel(){
        ViewModel.emailError.observe(this) { error ->
            emailInputLayout.error = error
        }


        ViewModel.passwError.observe(this) { error ->
            passwInputLayout.error = error
        }


        ViewModel.isLoginButtonOn.observe(this) { isOn->
            loginButton.isOn = isOn
        }


        viewModel.isLoading.observe(this) { isLoading ->


            progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE


            loginButton.isEnabled = !isLoading


            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
        }




        viewModel.loginSuccessEvent.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Login exitoso!", Toast.LENGTH_SHORT).show()


                startActivity(Intent(this, Inici::class.java))
                finish()


                viewModel.onLoginSuccessEventHandled()
            }
        }


    }


}
