package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class SettingsFragment : Fragment() {

    lateinit var btnGuarda : Button
    lateinit var btnSortir : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
    }

    private fun initComponents(view: View) {
        btnGuarda = view.findViewById(R.id.btnGuarda)
        btnSortir = view.findViewById(R.id.btnSortir)
    }




}