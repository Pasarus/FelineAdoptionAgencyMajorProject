package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_adoption_form_confirmation.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat

/**
 * A simple [Fragment] subclass.
 */
class AdoptionFormConfirmation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle: Bundle? = this.arguments

        val view = inflater.inflate(R.layout.fragment_adoption_form_confirmation, container, false)

        val cat: Cat? = bundle!!.getParcelable("cat")
        Picasso.get().load(cat!!.pictureUrl).into(view.confirmationFormImageOfCat)

        view.yesButton.setOnClickListener {
            // Send adoption information off
            val adoptionDocument = FirebaseFirestore.getInstance()
                .collection("adoptionProcesses")
                .document(DataService.INSTANCE.user!!.uid)
                .collection("adoptionProcesses")
                .document("cat"+cat.catId)
            createAdoptionProcessesDocument(cat, adoptionDocument)
            updateUserWithAdoptionProcessesReference(adoptionDocument)

            //navigate back to home page
            view.findNavController().navigate(R.id.homeFragment)
        }

        view.cancelButton.setOnClickListener {
            view.findNavController().navigate(R.id.homeFragment)
        }

        return view
    }

    private fun updateUserWithAdoptionProcessesReference(adoptionDocument: DocumentReference) {
        FirebaseFirestore.getInstance().collection("users")
            .document(DataService.INSTANCE.user!!.uid).update(
                "adoptionProcesses", FieldValue.arrayUnion(adoptionDocument))
    }

    private fun createAdoptionProcessesDocument(
        cat: Cat,
        adoptionDocument: DocumentReference
    ): AdoptionProcess {
        val status = mapOf(
            "accepted" to false,
            "pending" to true,
            "pendingReason" to "",
            "rejected" to false
        )
        val adoptionProcess = AdoptionProcess(status, cat)
        adoptionDocument.set(adoptionProcess)
        return adoptionProcess
    }
}
