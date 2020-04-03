package uk.ac.aber.dcs.mmp.faa.ui.catinfo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_card.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class CatCard(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val view: View = itemView
    private val catName: TextView = view.findViewById(R.id.catName)
    private val catAge: TextView = view.findViewById(R.id.catAge)
    private val catLocation: TextView = view.findViewById(R.id.catLocation)
    private val catPictureCard: ImageView = view.findViewById(R.id.catPictureCard)
    private val favouriteButton: ImageView = view.findViewById(R.id.faveButtonCard)
    private var featuredCatSaved = false
    lateinit var cat: Cat

    init {
        view.setOnClickListener { onClick() }
    }

    private fun onClick() {
        val navController = view.findNavController()
        val bundle = Bundle()
        bundle.putParcelable("cat", cat)
        navController.navigate(R.id.catCardInfoFragment, bundle)
    }

    fun bind (cat: Cat){
        catName.text = cat.catName
        catAge.text = convertMonthsNumberToUsableString(cat.catAgeMonths)
        catLocation.text = cat.location
        if (cat.pictureUrl != "" && cat.pictureUrl != null){
            Picasso.get().load(cat.pictureUrl).into(catPictureCard)
        }

        // Get if this cat is a saved/favourite cat.
        if (!DataService.INSTANCE.isCatFavourite(cat.catId)) {
            // Set to unfavoured button if not favourite already
            favouriteButton.setImageDrawable(ResourcesCompat.getDrawable(view.context.resources,
                R.drawable.ic_favorite_border_orange_24dp, null))
        } else {
            featuredCatSaved = true
        }

        if (DataService.INSTANCE.darkMode){
            val white = view.resources.getColor(R.color.white, null)
            itemView.catCard.setCardBackgroundColor(view.resources.getColor(R.color.darkCardBackground, null))
            catName.setTextColor(white)
            catAge.setTextColor(white)
            catLocation.setTextColor(white)
        }

        this.cat = cat

        favouriteButton.setOnClickListener {
            if (DataService.INSTANCE.user == null) {
                // We must login first
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                if (featuredCatSaved) {
                    // Perform un-saving
                    // Update local state
                    favouriteButton.setImageDrawable(view.resources
                        .getDrawable(R.drawable.ic_favorite_border_orange_24dp, null))
                    featuredCatSaved = false

                    // Update global state
                    DataService.INSTANCE.savedCats.remove(cat.catId!!)
                } else {
                    // Perform saving
                    // Update local state
                    favouriteButton.setImageDrawable(view.resources
                        .getDrawable(R.drawable.ic_favorite_orange_24dp, null))
                    featuredCatSaved = true

                    // Update global state
                    DataService.INSTANCE.savedCats.add(cat.catId!!)
                }
            }
        }
    }
}
