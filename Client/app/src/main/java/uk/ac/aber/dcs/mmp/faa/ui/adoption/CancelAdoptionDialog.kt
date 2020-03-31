package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cancel_adoption_dialog.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat

class CancelAdoptionDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cancel_adoption_dialog, container, false)

        val bundle = this.arguments
        val cat: Cat? = bundle!!.getParcelable("cat")

        val catName = cat!!.catName
        view.cancelAdoptionText.text = "Are you sure you want to cancel the adoption of $catName?"

        view.cancelButton.setOnClickListener {
            // Run a cancel query and navigate back to home page
            val query = FirebaseFirestore.getInstance().collection("adoptionProcesses")
                .document(DataService.INSTANCE.user!!.uid)
                .collection("adoptionProcesses")
                .document("cat" + cat.catId!!)

            query.delete().addOnSuccessListener {
                findNavController().navigate(R.id.homeFragment)
            }
            .addOnCanceledListener {
                // Do Nothing
            }
        }

        view.noButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }
}
