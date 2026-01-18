package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ActualitzaTasca(var item: Task) : DialogFragment() {

    lateinit var btnCancel: Button
    lateinit var btnSave: Button
    lateinit var btnDelete: Button
    lateinit var etNom: EditText
    lateinit var etSparks: EditText
    lateinit var etData: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_actualitza_tasca, null)
            val item = this.item

            btnCancel = view.findViewById(R.id.btnCancel)
            btnSave = view.findViewById(R.id.btnSave)
            btnDelete = view.findViewById(R.id.btnDelete)
            etNom = view.findViewById(R.id.etNom)
            etSparks = view.findViewById(R.id.etSparks)
            etData = view.findViewById(R.id.etData)
            etNom.setText


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

    private fun initListeners() {
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            dismiss()
        }
        btnDelete.setOnClickListener {
            dismiss()
        }
    }

}