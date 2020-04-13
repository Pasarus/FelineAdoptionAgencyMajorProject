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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adoption_status_card.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat

/**
 * A RecyclerView.ViewHolder implementation that produces the information required for the adoption
 * status view to be easy to read and when bind is called, it requires an AdoptionProcess object
 * that is provided as a snapshot of the data, but is guaranteed at time of bind being called.
 */
class AdoptionStatusCard(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val view: View = itemView
    private val catName: TextView = view.findViewById(R.id.adoptionStatusCardTitle)
    private val catImage: ImageView = view.findViewById(R.id.adoptionStatusCardCatImage)
    private val adoptionInfo: TextView = view.findViewById(R.id.adoptionStatusAdoptionInfo)
    private val adoptionStatusIcon: ImageView = view.findViewById(R.id.adoptionStatusStatusIcon)
    lateinit var adoptionProcess: AdoptionProcess
    lateinit var cat: Cat

    fun bind(model: AdoptionProcess) {
        adoptionProcess = model
        requestCatInfoAndBind(model.cat!!)
        val status = model.status!!
        val drawable = when {
            status["accepted"] as Boolean -> {
                adoptionInfo.setText(R.string.adoption_success)
                view.resources.getDrawable(R.drawable.ic_done_all_green_24dp, null)
            }
            status["pending"] as Boolean -> {
                adoptionInfo.text = status["pendingReason"].toString()
                view.resources.getDrawable(R.drawable.ic_access_alarm_yellow_24dp, null)
            }
            else -> {
                // Must have been rejected
                adoptionInfo.text = status["rejectedReason"].toString()
                view.resources.getDrawable(R.drawable.ic_highlight_off_red_24dp, null)
            }
        }
        adoptionStatusIcon.setImageDrawable(drawable)
        view.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("adoptionProcess", model)
            view.findNavController().navigate(R.id.adoptionStatusInfoViewFragment, bundle)
        }

        val white = view.resources.getColor(R.color.white, null)
        if (DataService.INSTANCE.darkMode) {
            view.adoptionProcessCard.setBackgroundColor(
                view.resources.getColor(
                    R.color.darkCardBackground,
                    null
                )
            )
            catName.setTextColor(white)
            adoptionInfo.setTextColor(white)
        }
    }

    private fun requestCatInfoAndBind(catReference: DocumentReference) {
        catReference.get().addOnSuccessListener { document ->
            cat = document.toObject(Cat::class.java)!!
            catName.text = cat.catName
            Picasso.get().load(cat.pictureUrl).into(catImage)
        }
    }
}
