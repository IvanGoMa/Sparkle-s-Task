package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.Locale


class CreateTask : DialogFragment() {

    lateinit var btnCancel: Button
    lateinit var btnCreate: Button
    lateinit var etNom: EditText
    lateinit var etSparks: EditText
    lateinit var etData: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_afegir_tasca, null)

            btnCancel = view.findViewById(R.id.btnCancel)
            btnCreate = view.findViewById(R.id.btnCreate)
            etNom = view.findViewById<EditText>(R.id.etNom)
            etSparks = view.findViewById<EditText>(R.id.etSparks)
            etData = view.findViewById<EditText>(R.id.etData)

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
}

private fun CreateTask.initListeners() {
    btnCancel.setOnClickListener {
        dismiss()
    }

    btnCreate.setOnClickListener {
        val df: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        TasksList.items.add(Task(df.parse(etData.text.toString())!!, etNom.text.toString(),etSparks.text.toString().toInt()))
        dismiss()
    }
}
