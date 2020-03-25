package uk.ac.aber.dcs.mmp.faa.ui.adoption

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.squareup.picasso.Picasso

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat

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
                view.resources.getDrawable(R.drawable.ic_done_all_green_24dp)
            }
            status["pending"] as Boolean -> {
                adoptionInfo.text = status["pendingReason"].toString()
                view.resources.getDrawable(R.drawable.ic_access_alarm_yellow_24dp)
            }
            else -> {
                // Must have been rejected
                adoptionInfo.text = status["rejectedReason"].toString()
                view.resources.getDrawable(R.drawable.ic_highlight_off_red_24dp)
            }
        }
        adoptionStatusIcon.setImageDrawable(drawable)
    }

    private fun requestCatInfoAndBind(catReference: DocumentReference){
        catReference.get().addOnSuccessListener{
            document ->
            cat = document.toObject(Cat::class.java)!!
            catName.text = cat.catName
            Picasso.get().load(cat.pictureUrl).into(catImage)
        }
    }
}
