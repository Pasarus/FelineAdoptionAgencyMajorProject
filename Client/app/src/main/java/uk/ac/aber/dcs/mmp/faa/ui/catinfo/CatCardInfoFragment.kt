package uk.ac.aber.dcs.mmp.faa.ui.catinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_card_info_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class CatCardInfoFragment : Fragment() {

    companion object {
        fun newInstance() = CatCardInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cat_card_info_fragment, container, false)

        val bundle: Bundle? = this.arguments
        val cat: Cat? = bundle!!.getParcelable("cat")

        if (cat != null) {
            Picasso.get().load(cat.pictureUrl).into(view.catInfoFragmentImage)
            view.catInfoFragmentCatName.text = cat.catName
            view.catInfoFragmentCatAge.text = convertMonthsNumberToUsableString(cat.catAgeMonths)
            view.catInfoFragmentCatLocation.text = cat.location
            view.catInfoFragmentCatDescription.text = cat.description
            view.catInfoFragmentSex.text = cat.sex

            if (cat.preferences!!["indoors"]!!)
                view.catInfoFragmentCanDealInside.text = resources.getText(R.string.canInfoCanDoIndoors)
            else
                view.catInfoFragmentCanDealInside.text = resources.getText(R.string.canInfoCannotDoIndoors)

            if (cat.preferences!!["dogs"]!!)
                view.catInfoFragmentCanDealWithDogs.text = resources.getText(R.string.catInfoCanBeWithDogs)
            else
                view.catInfoFragmentCanDealWithDogs.text = resources.getText(R.string.catInfoCannotBeWithDogs)

            if (cat.preferences!!["otherCats"]!!)
                view.catInfoFragmentCanDealWithCats.text = resources.getText(R.string.catInfoCanDealWithCats)
            else
                view.catInfoFragmentCanDealWithCats.text = resources.getText(R.string.catInfoCannotDealWithCats)

            if (cat.preferences!!["kids0to4"]!!)
                view.catInfoFragmentYoungKids.text = resources.getText(R.string.catInfoCanBeAroundYoungChildren)
            else
                view.catInfoFragmentYoungKids.text = resources.getText(R.string.catInfoCannotDealWithYoungChildren)

            if (cat.preferences!!["kids5to12"]!!)
                view.catInfoFragmentPrimaryAgeKids.text = resources.getText(R.string.catInfoCanBeAroundPrimaryAgeKids)
            else
                view.catInfoFragmentPrimaryAgeKids.text = resources.getText(R.string.catInfoCannnotBeAroundPrimaryAgeKids)

            if (cat.preferences!!["kids13to18"]!!)
                view.catInfoFragmentHighSchoolKids.text = resources.getText(R.string.catInfoCanBeAroundHighSchoolkids)
            else
                view.catInfoFragmentHighSchoolKids.text = resources.getText(R.string.catInfoCannotBeAroundHighSchoolKids)

            if (cat.disabled!!)
                view.catInfoFragmentDisabled.text = resources.getText(R.string.catInfoIsDisabled)
            else
                view.catInfoFragmentDisabled.text = resources.getText(R.string.catInfoIsntDisabled)

            if (cat.neutered!!)
                view.catInfoFragmentNeutered.text = resources.getText(R.string.catInfoIsNeutered)
            else
                view.catInfoFragmentNeutered.text = resources.getText(R.string.catInfoIsntNeutered)

        }

        return view
    }

}
