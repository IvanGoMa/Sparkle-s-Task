package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import cat.ivha.sparklestask.databinding.ActivityAfegirTascaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateTask : DialogFragment() {

    private var _binding: ActivityAfegirTascaBinding? = null
    private val binding get() = _binding!!

    private val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            _binding = ActivityAfegirTascaBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(activity)
            builder.setView(binding.root)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCreate.setOnClickListener {
            crearTasca()
        }
    }

    private fun crearTasca() {
        val nom = binding.etNom.text.toString()
        val sparksText = binding.etSparks.text.toString()
        val dataText = binding.etData.text.toString()


        if (nom.isBlank()) {
            Toast.makeText(context, "El nom no pot estar buit", Toast.LENGTH_SHORT).show()
            return
        }

        if (sparksText.isBlank()) {
            Toast.makeText(context, "Els sparks no poden estar buits", Toast.LENGTH_SHORT).show()
            return
        }

        val sparks = sparksText.toIntOrNull()
        if (sparks == null) {
            Toast.makeText(context, "Els sparks han de ser un número", Toast.LENGTH_SHORT).show()
            return
        }

        if (dataText.isBlank()) {
            Toast.makeText(context, "La data no pot estar buida", Toast.LENGTH_SHORT).show()
            return
        }

        val data = try {
            df.parse(dataText) ?: Date()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Format de data invàlid. Utilitza dd/MM/yyyy",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        val novaTasca = TaskRequest(
            data = data,
            title = nom,
            sparks = sparks
        )

        (parentFragment as? HomeFragment)?.afegirTasca(novaTasca)

        dismiss()
    }
}