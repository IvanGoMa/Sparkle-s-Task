package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class PerfilFragment : Fragment(R.layout.perfil) {

    lateinit var back : LinearLayout
    lateinit var ivHelp : ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = view.findViewById(R.id.back)
        ivHelp = view.findViewById(R.id.ivHelp)


        back.setOnClickListener {
            val intent = Intent(requireContext(), Menu::class.java)
            startActivity(intent)
        }
        ivHelp.setOnClickListener {
            val intent = Intent(requireContext(), PerfilHelp::class.java)
            startActivity(intent)
        }
    }
}