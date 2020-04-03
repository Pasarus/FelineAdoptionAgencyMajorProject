package uk.ac.aber.dcs.mmp.faa.ui.saved

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.saved_cat_card.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class SavedCatCard(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val catName = itemView.catName
    private val catAge = itemView.catAge
    private val catLocation = itemView.catLocation
    private val catImage = itemView.catImage
    private val catDescription = itemView.catDescription
    private val faveButtonCard = itemView.faveButtonCard
    private var catSaved = false
    private lateinit var catCopy: Cat

    init {
        faveButtonCard.setOnClickListener {
            if (DataService.INSTANCE.user == null) {
                // We must login first
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                if (catSaved) {
                    // Perform un-saving
                    // Update local state
                    faveButtonCard.setImageDrawable(
                        itemView.resources
                            .getDrawable(R.drawable.ic_favorite_border_orange_24dp, null)
                    )
                    catSaved = false

                    // Update global state
                    DataService.INSTANCE.savedCats.remove(catCopy.catId!!)
                } else {
                    // Perform saving
                    // Update local state
                    faveButtonCard.setImageDrawable(
                        itemView.resources
                            .getDrawable(R.drawable.ic_favorite_orange_24dp, null)
                    )
                    catSaved = true

                    // Update global state
                    DataService.INSTANCE.savedCats.add(catCopy.catId!!)
                }
            }
        }
    }

    fun bind (cat: Cat) {
        catName.text = cat.catName
        catAge.text = convertMonthsNumberToUsableString(cat.catAgeMonths)
        catLocation.text = cat.location
        Picasso.get().load(cat.pictureUrl).into(catImage)
        catDescription.text = cat.description

        if (DataService.INSTANCE.isCatFavourite(cat.catId)){
            // Update local state
            faveButtonCard.setImageDrawable(itemView.resources
                .getDrawable(R.drawable.ic_favorite_orange_24dp, null))
            catSaved = true
        }
        catCopy = cat

        // Set up darkMode for card
        if (DataService.INSTANCE.darkMode){
            itemView.savedCatCardViewLayout.setBackgroundColor(itemView.resources.getColor(R.color.darkCardBackground, null))
            catName.setTextColor(itemView.resources.getColor(R.color.white, null))
            catAge.setTextColor(itemView.resources.getColor(R.color.white, null))
            catLocation.setTextColor(itemView.resources.getColor(R.color.white, null))
            catDescription.setTextColor(itemView.resources.getColor(R.color.white, null))
        }

        itemView.setOnClickListener {
            val navController = itemView.findNavController()
            val bundle = Bundle()
            bundle.putParcelable("cat", cat)
            navController.navigate(R.id.catCardInfoFragment, bundle)
        }
    }
}