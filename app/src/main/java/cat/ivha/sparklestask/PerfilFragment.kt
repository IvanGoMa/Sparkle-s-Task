package cat.ivha.sparklestask

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.ivha.sparklestask.databinding.PerfilRvBinding


class PerfilFragment : Fragment(R.layout.perfil_rv) {

    lateinit var ivHelp : ImageView
    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView

    private var _binding: PerfilRvBinding? = null
    private val binding get() = _binding!!
    private var ultimClicat: String? = null
    private val collars: String = "Collars"
    private val ulleres: String = "Ulleres"
    private val gorros: String = "Gorros"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        _binding = PerfilRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivHelp = view.findViewById(R.id.ivHelp)
        recyclerView = view.findViewById(R.id.rvItems)
        val btnCollars = binding.btnCollars
        val btnUlleres = binding.btnUlleres
        val btnGorros = binding.btnGorros


        val items = DataSource.items
        adapter = MyAdapter(
            itemsComplets = items,
            onItemClick = { item ->
                Toast.makeText(
                    requireContext(),
                    item.desc,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)


        ivHelp.setOnClickListener {
            val intent = Intent(requireContext(), PerfilHelp::class.java)
            startActivity(intent)
        }
        ivHelp.setOnClickListener {
            val intent = Intent(requireContext(), PerfilHelp::class.java)
            startActivity(intent)
        }

        btnCollars.setOnClickListener {
            if (ultimClicat == collars){
                adapter.filtra(null)
                btnCollars.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = null
            } else {
                adapter.filtra(Categoria.COLLARS)
                btnCollars.setBackgroundResource(R.drawable.round_pink_sec)
                btnUlleres.setBackgroundResource(R.drawable.round_pink)
                btnGorros.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = collars
            }
        }

        btnUlleres.setOnClickListener {

            if (ultimClicat == ulleres){
                adapter.filtra(null)
                btnUlleres.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = null
            } else {
                adapter.filtra(Categoria.ULLERES)
                btnUlleres.setBackgroundResource(R.drawable.round_pink_sec)
                btnCollars.setBackgroundResource(R.drawable.round_pink)
                btnGorros.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = ulleres
            }
        }

        btnGorros.setOnClickListener {
            if (ultimClicat == gorros){
                adapter.filtra(null)
                btnGorros.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = null
            } else {
                adapter.filtra(Categoria.GORROS)
                btnGorros.setBackgroundResource(R.drawable.round_pink_sec)
                btnCollars.setBackgroundResource(R.drawable.round_pink)
                btnUlleres.setBackgroundResource(R.drawable.round_pink)
                ultimClicat = gorros
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}