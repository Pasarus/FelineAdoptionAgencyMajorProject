package uk.ac.aber.dcs.mmp.faa.ui.saved

import android.view.View
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
            if (catSaved) {
                // Saved, so un-save
                catSaved = false
                faveButtonCard.setImageDrawable(itemView.resources
                    .getDrawable(R.drawable.ic_favorite_border_black_24dp, null))
                DataService.INSTANCE.savedCats.remove(catCopy.catId)
            } else {
                // Not saved, so save
                faveButtonCard.setImageDrawable(itemView.resources
                    .getDrawable(R.drawable.ic_favorite_black_24dp, null))
                catSaved = true
                DataService.INSTANCE.savedCats.add(catCopy.catId!!)
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
                .getDrawable(R.drawable.ic_favorite_black_24dp, null))
            catSaved = true
        }
        catCopy = cat
    }
}