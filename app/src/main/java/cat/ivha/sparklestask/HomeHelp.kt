package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeHelp : AppCompatActivity() {

    lateinit var ivSortir : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initComponents()
        initListeners()



    }

    private fun initListeners() {
        ivSortir.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        ivSortir = findViewById(R.id.ivSortir)
    }


}