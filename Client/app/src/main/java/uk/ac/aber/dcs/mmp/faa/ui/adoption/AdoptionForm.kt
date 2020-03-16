package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.user_data.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

/**
 * A simple [Fragment] subclass.
 */
class AdoptionForm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_adoption_form, container, false)

        val bundle: Bundle? = this.arguments

        DataService.INSTANCE.getDataForViewFromFirestore(view)
        view.updateAccountInformationButton.setOnClickListener {
            // Grab all the data for the database
            DataService.INSTANCE.updateUserDataForView(view)

            // Navigate to confirmation screen
            view.findNavController().navigate(R.id.adoptionFormConfirmation, bundle)
        }

        return view
    }

}
