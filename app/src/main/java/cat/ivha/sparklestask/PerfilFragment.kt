package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PerfilFragment : Fragment(R.layout.perfil_rv) {

    lateinit var back : LinearLayout
    lateinit var ivHelp : ImageView
    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCollars: Button
    private lateinit var btnGorros: Button
    private lateinit var btnUlleres: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.perfil_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivHelp = view.findViewById(R.id.ivHelp)
        recyclerView = view.findViewById(R.id.rvItems)

        val items = DataSource.items
        adapter = MyAdapter(
            itemsComplets = items,
            onItemClick = { item ->
                Toast.makeText(
                    requireContext(),
                    "Has clicat",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)


        ivHelp.setOnClickListener {
            val intent = Intent(requireContext(), PerfilHelp::class.java)
            startActivity(intent)
        }
        ivHelp.setOnClickListener {
            val intent = Intent(requireContext(), PerfilHelp::class.java)
            startActivity(intent)
        }
    }
}