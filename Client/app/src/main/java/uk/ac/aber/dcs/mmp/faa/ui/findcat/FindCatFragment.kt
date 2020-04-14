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

package uk.ac.aber.dcs.mmp.faa.ui.findcat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.find_cat_fragment.view.*
import kotlinx.android.synthetic.main.search_filter_layout.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.ui.catinfo.CatCard

/**
 * This is the main feature of the app, it provides real time information for what cats are
 * currently listed for adoption, at present it provides the functionality to search and filter for
 * specific cats based on multiple categories. It utilises a FirestoreRecyclerAdapter that is
 * updated everytime the filters change to provide a new up to date look at the data.
 */
class FindCatFragment : Fragment() {

    private var filtersShowing = false

    override fun onStart() {
        super.onStart()
        clearFilters(view!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_cat_fragment, container, false)

        setupTheFilterMenus(view)
        updateRecyclerViewAdapterWithFilters(view)

        view.updateFilterButton.setOnClickListener {
            updateRecyclerViewAdapterWithFilters(view)
            if(filtersShowing) {
                closeFilters(view)
                filtersShowing = false
            }
        }

        view.noFilterButton.setOnClickListener {
            clearFilters(view)
            if(filtersShowing) {
                closeFilters(view)
                filtersShowing = false
            }
            updateRecyclerViewAdapterWithFilters(view)
        }

        view.filterSearchText.setOnClickListener {
            filtersShowing = if (!filtersShowing){
                showFilters(view)
                true
            } else {
                closeFilters(view)
                false
            }
        }

        return view
    }

    private fun clearFilters(view: View) {
        view.searchTextField.setText("")
        view.locationFilterDropDown.setText("")
        view.familiesFilterDropDown.setText("")
        view.dogsFilterDropDown.setText("")
        view.disabledFilterDropDown.setText("")
        view.otherCatsFilterDropDown.setText("")
        view.indoorsFilterDropDown.setText("")
    }

    private fun closeFilters(view: View) {
        view.filterLayout.visibility = View.GONE
        val openFiltersIcon = view.resources.getDrawable(R.drawable.ic_expand_more_black_24dp,null)
        view.searchTextField.setCompoundDrawables(null, null, openFiltersIcon, null)
    }

    private fun showFilters(view: View){
        view.filterLayout.visibility = View.VISIBLE
        val openFiltersIcon = view.resources.getDrawable(R.drawable.ic_expand_less_black_24dp,null)
        view.searchTextField.setCompoundDrawables(null, null, openFiltersIcon, null)
    }

    private fun setupTheFilterMenus(view: View) {
        val context = view.context
        val menuItem = R.layout.dropdown_filter_item

        val familiesItems = resources.getStringArray(R.array.families)
        val disabledItems = resources.getStringArray(R.array.disabled)
        val otherCatsItems = resources.getStringArray(R.array.otherCats)
        val dogsItems = resources.getStringArray(R.array.dogs)
        val indoorsOnlyItems = resources.getStringArray(R.array.indoorsOnly)
        val locationItems = resources.getStringArray(R.array.location)
        val sortByItems = resources.getStringArray(R.array.sortBy)
        val sortByOrderingItems = resources.getStringArray(R.array.sortByOrdering)

        view.familiesFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, familiesItems))
        view.disabledFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, disabledItems))
        view.otherCatsFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, otherCatsItems))
        view.dogsFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, dogsItems))
        view.indoorsFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, indoorsOnlyItems))
        view.locationFilterDropDown.setAdapter(ArrayAdapter(context, menuItem, locationItems))
        view.sortByDropDown.setAdapter(ArrayAdapter(context, menuItem, sortByItems))
        view.sortByDropDown.setText("Recently Listed", false)
        view.sortByOrderingDropDown.setAdapter(ArrayAdapter(context, menuItem, sortByOrderingItems))
        view.sortByOrderingDropDown.setText("Ascending", false)
    }

    private fun updateRecyclerViewAdapterWithFilters(view: View) {
        // Get map from field
        val filterMap = grabMapFromFilterObjects(view)

        val query = generateQueryBasedOnMap(filterMap)

        val options = FirestoreRecyclerOptions.Builder<Cat>().setQuery(query, Cat::class.java)
            .setLifecycleOwner(this).build()

        val adapter = object : FirestoreRecyclerAdapter<Cat, CatCard>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatCard {
                val cardView =
                    LayoutInflater.from(parent.context).inflate(R.layout.cat_card, parent, false)
                return CatCard(cardView)
            }

            override fun onBindViewHolder(holder: CatCard, position: Int, model: Cat) {
                holder.bind(model)
            }
        }

        view.catRecyclerView.adapter = adapter
    }

    private fun generateQueryBasedOnMap(map: Map<String, String>): Query {
        var query: Query = FirebaseFirestore.getInstance().collection("cats")

        val location = map["location"]
        if (location != "") {
            query = query.whereEqualTo("location", location)
        }

        val families = map["families"]
        if (families != "") {
            if (families == "Children") {
                query = query.whereEqualTo("kids0to4", true)
                query = query.whereEqualTo("kids5to12", true)
                query = query.whereEqualTo("kids13to18", true)
            } else if (families == "No Children") {
                query = query.whereEqualTo("kids0to4", false)
                query = query.whereEqualTo("kids5to12", false)
                query = query.whereEqualTo("kids13to18", false)
            }
        }

        val otherCats = map["otherCats"]
        if (otherCats != "") {
            if (otherCats == "Other cats welcome") {
                query = query.whereEqualTo("otherCats", true)
            } else if (otherCats == "No other cats") {
                query = query.whereEqualTo("otherCats", false)
            }
        }

        val dogs = map["dogs"]
        if (dogs != "") {
            if (dogs == "Happy with Dogs") {
                query = query.whereEqualTo("dogs", true)
            } else if (dogs == "Unhappy with Dogs") {
                query = query.whereEqualTo("dogs", false)
            }
        }

        val indoors = map["indoorsOnly"]
        if (indoors != "") {
            if (indoors == "Indoors only") {
                query = query.whereEqualTo("indoors", true)
            } else if (indoors == "Needs outside access") {
                query = query.whereEqualTo("indoors", false)
            }
        }

        val sortBy = map["sortBy"]
        var orderByString = ""
        when (sortBy) {
            "Name" -> {
                orderByString = "catName"
            }
            "Age" -> {
                orderByString = "catAgeMonths"
            }
            "Recently Listed" -> {
                orderByString = "catId"
            }
        }

        query = if (map["sortByOrdering"] == "Ascending"){
            query.orderBy(orderByString, Query.Direction.ASCENDING)
        } else {
            query.orderBy(orderByString, Query.Direction.DESCENDING)
        }

        val name = map["name"]
        if (name != "") {
            query = query.startAt(name).endAt(name + "\uf8ff")
        }

        return query
    }

    private fun grabMapFromFilterObjects(view: View): Map<String, String> {
        return mapOf(
            "name" to view.searchTextField.text.toString(),
            "location" to view.locationFilterDropDown.text.toString(),
            "families" to view.familiesFilterDropDown.text.toString(),
            "disabled" to view.disabledFilterDropDown.text.toString(),
            "otherCats" to view.otherCatsFilterDropDown.text.toString(),
            "indoorsOnly" to view.indoorsFilterDropDown.text.toString(),
            "dogs" to view.dogsFilterDropDown.text.toString(),
            "sortBy" to view.sortByDropDown.text.toString(),
            "sortByOrdering" to view.sortByOrderingDropDown.text.toString()
        )
    }
}
