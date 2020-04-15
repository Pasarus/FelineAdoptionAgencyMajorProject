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

package uk.ac.aber.dcs.mmp.faa.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_fragment.view.adoptionStatusRecyclerView
import kotlinx.android.synthetic.main.my_account_fragment.view.*
import kotlinx.android.synthetic.main.user_data.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.ui.adoption.AdoptionStatusCard

/**
 * This shows your current account information, as well as allowing you to update your address and
 * phone number. It utilizes a recyclerview with a FirestoreRecyclerAdapter that is aimed at
 * providing up to date information from the Firestore for your current adoption statuses.
 *
 * The data is displayed in EditText fields based on material design and it's implementation.
 */
class MyAccountFragment : Fragment() {

    private val query = FirebaseFirestore.getInstance().collection("adoptionProcesses")
        .document(DataService.INSTANCE.user!!.uid).collection("adoptionProcesses").limit(10)

    private val options = FirestoreRecyclerOptions.Builder<AdoptionProcess>()
        .setQuery(query, AdoptionProcess::class.java).setLifecycleOwner(this).build()

    private val adapter =
        object : FirestoreRecyclerAdapter<AdoptionProcess, AdoptionStatusCard>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionStatusCard {
                val localView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adoption_status_card, parent, false)
                return AdoptionStatusCard(localView)
            }

            override fun onBindViewHolder(
                holder: AdoptionStatusCard,
                position: Int,
                model: AdoptionProcess
            ) {
                holder.bind(model)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_account_fragment, container, false)

        DataService.INSTANCE.getDataForViewFromFirestore(view)
        view.updateAccountInformationButton.setOnClickListener {
            // Grab all the data
            DataService.INSTANCE.updateUserDataForView(view)
        }
        view.adoptionStatusRecyclerView.adapter = adapter

        view.logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            DataService.INSTANCE.user = null
            DataService.INSTANCE.mainActivity.updateNavDrawLoginTextAndImage(null)
            findNavController().navigate(R.id.homeFragment)
        }

        val white = ContextCompat.getColor(context!!, R.color.white)
        if (DataService.INSTANCE.darkMode) {
            view.accountTitle.setTextColor(white)
            view.accountInformation.setTextColor(white)
        }

        return view
    }


}
