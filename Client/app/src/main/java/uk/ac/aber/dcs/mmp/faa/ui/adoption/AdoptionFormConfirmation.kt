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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_adoption_form_confirmation.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat

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
                .document("cat" + cat.catId)
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
                "adoptionProcesses", FieldValue.arrayUnion(adoptionDocument)
            )
    }

    private fun createAdoptionProcessesDocument(
        cat: Cat,
        adoptionDocument: DocumentReference
    ): AdoptionProcess {
        val status = mapOf(
            "accepted" to false,
            "pending" to true,
            "pendingReason" to "Please await a call with an administrator to arrange an appointment!",
            "rejected" to false,
            "rejectedReason" to ""
        )
        val adoptionProcess = AdoptionProcess(status, cat)
        adoptionDocument.set(adoptionProcess)
        return adoptionProcess
    }
}
