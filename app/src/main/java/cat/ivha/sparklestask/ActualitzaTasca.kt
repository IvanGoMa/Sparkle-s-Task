package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class ActualitzaTasca() : DialogFragment() {

    // Vistes
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var etNom: EditText
    private lateinit var etSparks: EditText
    private lateinit var etData: EditText
    private val viewModel: HomeViewModel by activityViewModels()

    private val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Dades de la tasca
    private var taskId: Long = -1
    private var taskTitle: String = ""
    private var taskSparks: Int = 0
    private var taskDate: Long = 0L

    companion object {
        private const val ARG_TASK_ID = "task_id"
        private const val ARG_TASK_TITLE = "task_title"
        private const val ARG_TASK_SPARKS = "task_sparks"
        private const val ARG_TASK_DATE = "task_date"

        fun newInstance(task: Task): ActualitzaTasca {
            return ActualitzaTasca().apply {
                arguments = Bundle().apply {
                    putLong(ARG_TASK_ID, task.id)
                    putString(ARG_TASK_TITLE, task.nomTasca)
                    putLong(ARG_TASK_SPARKS, task.sparks)
                    putLong(ARG_TASK_DATE, task.dataLimit.time)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            // Recuperar dades del Bundle
            recuperarDadesDelBundle()

            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_actualitza_tasca, null)

            // Inicialitzar vistes
            btnCancel = view.findViewById(R.id.btnCancel)
            btnSave = view.findViewById(R.id.btnSave)
            btnDelete = view.findViewById(R.id.btnDelete)
            etNom = view.findViewById(R.id.etNom)
            etSparks = view.findViewById(R.id.etSparks)
            etData = view.findViewById(R.id.etData)

            // Omplir camps
            etNom.setText(taskTitle)
            etSparks.setText(taskSparks.toString())
            etData.setText(df.format(Date(taskDate)))

            builder.setView(view)
            initListeners()

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun recuperarDadesDelBundle() {
        arguments?.let { args ->
            taskId = args.getLong(ARG_TASK_ID, -1)
            taskTitle = args.getString(ARG_TASK_TITLE, "")
            taskSparks = args.getInt(ARG_TASK_SPARKS, 0)
            taskDate = args.getLong(ARG_TASK_DATE, System.currentTimeMillis())
        }
    }

    private fun initListeners() {
        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {
            guardarCanvis()
        }

        btnDelete.setOnClickListener {
            confirmarEliminacio()
        }
    }

    private fun guardarCanvis() {
        val nouNom = etNom.text.toString()
        val nousSparksText = etSparks.text.toString()
        val novaDataText = etData.text.toString()

        // Validacions
        if (nouNom.isBlank()) {
            Toast.makeText(context, "El nom no pot estar buit", Toast.LENGTH_SHORT).show()
            return
        }

        val nousSparks = nousSparksText.toLongOrNull()
        if (nousSparks == null) {
            Toast.makeText(context, "Els sparks han de ser un número", Toast.LENGTH_SHORT).show()
            return
        }

        val novaData = try {
            df.parse(novaDataText) ?: Date(taskDate)
        } catch (e: Exception) {
            Toast.makeText(context, "Format de data invàlid", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear tasca actualitzada
        val tascaActualitzada = Task(
            dataLimit = novaData,
            nomTasca = nouNom,
            sparks = nousSparks,
            id = taskId
        )

        (parentFragment as? HomeFragment)?.actualizarTarea(tascaActualitzada)

        dismiss()
    }

    private fun confirmarEliminacio() {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar tasca")
            .setMessage("Estàs segur que vols eliminar aquesta tasca?")
            .setPositiveButton("Eliminar") { _, _ ->
                (parentFragment as? HomeFragment)?.eliminarTarea(taskId)
                dismiss()
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }
}