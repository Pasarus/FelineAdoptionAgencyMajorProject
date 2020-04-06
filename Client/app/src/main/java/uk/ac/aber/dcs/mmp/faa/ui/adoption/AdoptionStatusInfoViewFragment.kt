/*   Copyright 2020 Samuel Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adoption_status_info_view_fragment.view.*
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
            status!!["pending"] as Boolean -> {
                super.onResume()
                statusDrawable = R.drawable.ic_access_alarm_yellow_24dp
                adoptionDescription = status["pendingReason"] as String
                adoptionStatus += "Pending"
            }
            status["accepted"] as Boolean -> {
                statusDrawable = R.drawable.ic_done_all_green_24dp
                adoptionDescription =
                    "This cat is ready to collect or has already been collected! If you haven't yet please collect the cat from the Cattery!"
                adoptionStatus += "Accepted"
            }
            else -> {
                statusDrawable = R.drawable.ic_highlight_off_red_24dp
                adoptionDescription = status["rejectedReason"] as String
                adoptionStatus += "Rejected"
            }
        }
        view.adoptionStatusCurrentStatus.text = adoptionStatus
        view.adoptionStatusCurrentStatus.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            statusDrawable,
            0
        )
        view.adoptionStatusCurrentStatusDetails.text = adoptionDescription

        view.cancelButton.setOnClickListener {
            val cancelBundle = Bundle()
            cancelBundle.putParcelable("cat", cat)
            view.findNavController().navigate(R.id.cancel_adoption_dialog, cancelBundle)
        }

        view.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Setup Dark mode
        if (DataService.INSTANCE.darkMode) {
            val white = view.resources.getColor(R.color.white, null)
            view.adoptionStatusInfoFragmentCatName.setTextColor(white)
            view.adoptionStatusCurrentStatus.setTextColor(white)
        }

        return view
    }

    private fun requestCatUsingDocRef(catReference: DocumentReference, view: View) {
        catReference.get().addOnSuccessListener { document ->
            cat = document.toObject()
            view.adoptionStatusInfoFragmentCatName.text = cat!!.catName
            Picasso.get().load(cat!!.pictureUrl).into(view.adoptionStatusInfoFragmentImage)
            view.adoptionStatusInfoFragmentCatAge.text =
                convertMonthsNumberToUsableString(cat!!.catAgeMonths)
            view.adoptionStatusInfoFragmentCatLocation.text = cat!!.location
        }
    }

}
