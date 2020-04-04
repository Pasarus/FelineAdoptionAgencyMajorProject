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

package uk.ac.aber.dcs.mmp.faa.ui.catinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_card_info_fragment.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class CatCardInfoFragment : Fragment() {

    private var catSaved = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cat_card_info_fragment, container, false)

        val bundle: Bundle? = this.arguments
        val cat: Cat? = bundle!!.getParcelable("cat")

        Picasso.get().load(cat!!.pictureUrl).into(view.catInfoFragmentImage)
        view.catInfoFragmentCatName.text = cat.catName
        view.catInfoFragmentCatAge.text = convertMonthsNumberToUsableString(cat.catAgeMonths)
        view.catInfoFragmentCatLocation.text = cat.location
        view.catInfoFragmentCatDescription.text = cat.description
        view.catInfoFragmentSex.text = cat.sex

        if (cat.preferences!!["indoors"]!!)
            view.catInfoFragmentCanDealInside.text = resources.getText(R.string.canInfoCanDoIndoors)
        else
            view.catInfoFragmentCanDealInside.text =
                resources.getText(R.string.canInfoCannotDoIndoors)

        if (cat.preferences!!["dogs"]!!)
            view.catInfoFragmentCanDealWithDogs.text =
                resources.getText(R.string.catInfoCanBeWithDogs)
        else
            view.catInfoFragmentCanDealWithDogs.text =
                resources.getText(R.string.catInfoCannotBeWithDogs)

        if (cat.preferences!!["otherCats"]!!)
            view.catInfoFragmentCanDealWithCats.text =
                resources.getText(R.string.catInfoCanDealWithCats)
        else
            view.catInfoFragmentCanDealWithCats.text =
                resources.getText(R.string.catInfoCannotDealWithCats)

        if (cat.preferences!!["kids0to4"]!!)
            view.catInfoFragmentYoungKids.text =
                resources.getText(R.string.catInfoCanBeAroundYoungChildren)
        else
            view.catInfoFragmentYoungKids.text =
                resources.getText(R.string.catInfoCannotDealWithYoungChildren)

        if (cat.preferences!!["kids5to12"]!!)
            view.catInfoFragmentPrimaryAgeKids.text =
                resources.getText(R.string.catInfoCanBeAroundPrimaryAgeKids)
        else
            view.catInfoFragmentPrimaryAgeKids.text =
                resources.getText(R.string.catInfoCannnotBeAroundPrimaryAgeKids)

        if (cat.preferences!!["kids13to18"]!!)
            view.catInfoFragmentHighSchoolKids.text =
                resources.getText(R.string.catInfoCanBeAroundHighSchoolkids)
        else
            view.catInfoFragmentHighSchoolKids.text =
                resources.getText(R.string.catInfoCannotBeAroundHighSchoolKids)

        if (cat.disabled!!)
            view.catInfoFragmentDisabled.text = resources.getText(R.string.catInfoIsDisabled)
        else
            view.catInfoFragmentDisabled.text = resources.getText(R.string.catInfoIsntDisabled)

        if (cat.neutered!!)
            view.catInfoFragmentNeutered.text = resources.getText(R.string.catInfoIsNeutered)
        else
            view.catInfoFragmentNeutered.text = resources.getText(R.string.catInfoIsntNeutered)


        view.catInfoFragmentAdoptButton.setOnClickListener {
            //Navigate to adopt form but pass this cat id to it
            if (DataService.INSTANCE.user == null) {
                // Ask to Login
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                view.findNavController().navigate(R.id.adoptionForm, bundle)
            }
        }

        // Get if this cat is a saved/favourite cat.
        if (DataService.INSTANCE.isCatFavourite(cat.catId)) {
            // Set to unfavoured button if not favourite already
            view.faveButton.setImageDrawable(
                resources
                    .getDrawable(R.drawable.ic_favorite_orange_24dp, null)
            )
            catSaved = true
        }


        view.faveButton.setOnClickListener {
            if (DataService.INSTANCE.user == null) {
                // We must login first
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                if (catSaved) {
                    // Saved, so un-save
                    catSaved = false
                    view.faveButton.setImageDrawable(
                        resources
                            .getDrawable(R.drawable.ic_favorite_border_orange_24dp, null)
                    )
                    DataService.INSTANCE.savedCats.remove(cat.catId)
                } else {
                    // Not saved, so save
                    view.faveButton.setImageDrawable(
                        resources
                            .getDrawable(R.drawable.ic_favorite_orange_24dp, null)
                    )
                    catSaved = true
                    DataService.INSTANCE.savedCats.add(cat.catId!!)
                }
            }
        }

        // Setup Dark mode
        if (DataService.INSTANCE.darkMode) {
            val white = view.resources.getColor(R.color.white, null)
            view.catInfoFragmentCatName.setTextColor(white)
        }

        return view
    }

}
