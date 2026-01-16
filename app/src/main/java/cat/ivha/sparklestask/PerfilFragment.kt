package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PerfilFragment : Fragment(R.layout.perfil_rv) {

    lateinit var ivHelp : ImageView
    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCollars: Button
    private lateinit var btnGorros: Button
    private lateinit var btnUlleres: Button
    private var clicat: Boolean = false;


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
        btnCollars = view.findViewById(R.id.btnCollars)
        btnUlleres = view.findViewById(R.id.btnUlleres)
        btnGorros = view.findViewById(R.id.btnGorros)


        val items = DataSource.items
        adapter = MyAdapter(
            itemsComplets = items,
            onItemClick = { item ->
                Toast.makeText(
                    requireContext(),
                    "Has clicat ${item.nom}",
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

        btnCollars.setOnClickListener {
            adapter.filtra(if (!clicat) Categoria.COLLARS else null)
            clicat = !clicat
        }

        btnUlleres.setOnClickListener {
            adapter.filtra(if (!clicat) Categoria.ULLERES else null)
            clicat = !clicat
        }

        btnGorros.setOnClickListener {
            adapter.filtra(if (!clicat) Categoria.GORROS else null)
            clicat = !clicat
        }
    }
}