package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class CreateTask : DialogFragment() {

    lateinit var btnCancel: Button
    lateinit var btnCreate: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_afegir_tasca, null)

            btnCancel = view.findViewById(R.id.btnCancel)
            btnCreate = view.findViewById(R.id.btnCreate)

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
        dismiss()
    }
}
