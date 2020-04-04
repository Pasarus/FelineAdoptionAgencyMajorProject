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

package uk.ac.aber.dcs.mmp.faa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_card.view.catName
import kotlinx.android.synthetic.main.home_fragment.view.*
import kotlinx.android.synthetic.main.saved_cat_card.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.ui.adoption.AdoptionStatusCard
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString
import kotlin.random.Random

class HomeFragment : Fragment() {
    private var featuredCatSaved = false

    private lateinit var layoutView: View

    private var query =
        FirebaseFirestore.getInstance().collection("adoptionProcesses").document("default")
            .collection("adoptionProcesses").limit(10)

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

    lateinit var cat: Cat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        view.adoptionStatusRecyclerView.adapter = adapter
        layoutView = view

        val white = view.resources.getColor(R.color.white, null)
        // Setup DarkMode
        if (DataService.INSTANCE.darkMode) {
            view.featuredCatTitleText.setTextColor(white)
            view.adoptionStatusTitle.setTextColor(white)
        }

        //Set up featured cat
        val randomInt = Random.nextInt(1, 9)
        val catToGet = "cat$randomInt"
        val catQuery = FirebaseFirestore.getInstance().collection("cats").document(catToGet)
        catQuery.get().addOnSuccessListener { document ->
            cat = document.toObject()!!
            view.catName.text = cat.catName
            view.catAge.text = convertMonthsNumberToUsableString(cat.catAgeMonths)
            view.catLocation.text = cat.location
            Picasso.get().load(cat.pictureUrl).into(view.catImage)
            view.catDescription.text = cat.description

            if (DataService.INSTANCE.isCatFavourite(cat.catId)) {
                // Update local state
                view.faveButtonCard.setImageDrawable(
                    resources
                        .getDrawable(R.drawable.ic_favorite_orange_24dp, null)
                )
                featuredCatSaved = true
            }

            // Setup Card Dark mode
            if (DataService.INSTANCE.darkMode) {
                view.savedCatCardViewLayout.setBackgroundColor(
                    view.resources.getColor(
                        R.color.darkCardBackground,
                        null
                    )
                )
                view.catName.setTextColor(white)
                view.catAge.setTextColor(white)
                view.catLocation.setTextColor(white)
                view.catDescription.setTextColor(white)
            }
        }


        view.featuredCatCardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("cat", cat)
            view.findNavController().navigate(R.id.catCardInfoFragment, bundle)
        }

        view.featuredCatCardView.faveButtonCard.setOnClickListener {
            if (DataService.INSTANCE.user == null) {
                // We must login first
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                if (featuredCatSaved) {
                    // Perform un-saving
                    // Update local state
                    view.faveButtonCard.setImageDrawable(
                        resources
                            .getDrawable(R.drawable.ic_favorite_border_orange_24dp, null)
                    )
                    featuredCatSaved = false

                    // Update global state
                    DataService.INSTANCE.savedCats.remove(cat.catId!!)
                } else {
                    // Perform saving
                    // Update local state
                    view.faveButtonCard.setImageDrawable(
                        resources
                            .getDrawable(R.drawable.ic_favorite_orange_24dp, null)
                    )
                    featuredCatSaved = true

                    // Update global state
                    DataService.INSTANCE.savedCats.add(cat.catId!!)
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val user = DataService.INSTANCE.user
        val reyclerView = layoutView.findViewById<RecyclerView>(R.id.adoptionStatusRecyclerView)
        val noUserText = layoutView.findViewById<TextView>(R.id.noUserYetTextView)
        if (user != null) {
            // Hide text and setup recycler view as the user is signed in.
            reyclerView.visibility = VISIBLE
            noUserText.visibility = INVISIBLE
            val query =
                FirebaseFirestore.getInstance().collection("adoptionProcesses").document(user.uid)
                    .collection("adoptionProcesses").limit(10)

            val options = FirestoreRecyclerOptions.Builder<AdoptionProcess>()
                .setQuery(query, AdoptionProcess::class.java).setLifecycleOwner(this).build()

            val adapter =
                object : FirestoreRecyclerAdapter<AdoptionProcess, AdoptionStatusCard>(options) {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): AdoptionStatusCard {
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
            reyclerView.adapter = adapter
        } else {
            reyclerView.visibility = INVISIBLE
            noUserText.visibility = VISIBLE
        }
    }

}
