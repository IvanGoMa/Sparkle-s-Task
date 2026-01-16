package cat.ivha.sparklestask

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
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
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            val view = inflater.inflate(R.layout.activity_afegir_tasca, null)
            btnCancel = view.findViewById<Button>(R.id.btnCancel)
            btnCreate = view.findViewById<Button>(R.id.btnCreate)
            builder.setView(view)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}