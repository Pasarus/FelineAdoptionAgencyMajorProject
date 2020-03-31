package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adoption_status_info_view_fragment.view.*
import kotlinx.android.synthetic.main.saved_cat_card.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class AdoptionStatusInfoViewFragment : Fragment() {
    var cat: Cat? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = this.arguments

        val view = inflater.inflate(R.layout.adoption_status_info_view_fragment, container, false)

        val adoptionProcess: AdoptionProcess? = bundle!!.getParcelable("adoptionProcess")
        requestCatUsingDocRef(adoptionProcess!!.cat!!, view)
        val status = adoptionProcess.status
        var adoptionStatus = "Adoption Status: "
        val adoptionDescription: String
        val statusDrawable: Int
        when {
            status!!["pending"] as Boolean -> {super.onResume()
                statusDrawable = R.drawable.ic_access_alarm_yellow_24dp
                adoptionDescription = status["pendingReason"] as String
                adoptionStatus += "Pending"
            }
            status["accepted"] as Boolean -> {
                statusDrawable = R.drawable.ic_done_all_green_24dp
                adoptionDescription = "This cat is ready to collect or has already been collected! If you haven't yet please collect the cat from the Cattery!"
                adoptionStatus += "Accepted"
            }
            else -> {
                statusDrawable = R.drawable.ic_highlight_off_red_24dp
                adoptionDescription = status["rejectedReason"] as String
                adoptionStatus += "Rejected"
            }
        }
        view.adoptionStatusCurrentStatus.text = adoptionStatus
        view.adoptionStatusCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, statusDrawable, 0)
        view.adoptionStatusCurrentStatusDetails.text = adoptionDescription

        view.cancelButton.setOnClickListener {
            val cancelBundle = Bundle()
            cancelBundle.putParcelable("cat", cat)
            view.findNavController().navigate(R.id.cancel_adoption_dialog, cancelBundle)
        }

        return view
    }

    private fun requestCatUsingDocRef(catReference: DocumentReference, view: View) {
        catReference.get().addOnSuccessListener {
            document ->
            cat = document.toObject()
            view.adoptionStatusInfoFragmentCatName.text = cat!!.catName
            Picasso.get().load(cat!!.pictureUrl).into(view.adoptionStatusInfoFragmentImage)
            view.adoptionStatusInfoFragmentCatAge.text =
                convertMonthsNumberToUsableString(cat!!.catAgeMonths)
            view.adoptionStatusInfoFragmentCatLocation.text = cat!!.location
        }
    }

}
