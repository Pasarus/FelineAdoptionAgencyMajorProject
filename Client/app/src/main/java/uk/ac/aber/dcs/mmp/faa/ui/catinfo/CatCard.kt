package uk.ac.aber.dcs.mmp.faa.ui.catinfo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
                R.drawable.ic_favorite_border_black_24dp, null))
        }

        this.cat = cat
    }
}
