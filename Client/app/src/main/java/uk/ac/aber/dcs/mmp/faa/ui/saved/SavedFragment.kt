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

package uk.ac.aber.dcs.mmp.faa.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.saved_fragment.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.ObserverOfStringSet

class SavedFragment : Fragment(), ObserverOfStringSet {

    private lateinit var thisView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.saved_fragment, container, false)

        thisView = view

        DataService.INSTANCE.savedCats.addObserver(this)

        return view
    }

    override fun onStart() {
        super.onStart()
        updateRecycleView()
        if (DataService.INSTANCE.user != null) {
            thisView.savedCatRecyclerView.visibility = View.VISIBLE
            thisView.savedCatNotSignedIn.visibility = View.INVISIBLE
        } else {
            thisView.savedCatRecyclerView.visibility = View.INVISIBLE
            thisView.savedCatNotSignedIn.visibility = View.VISIBLE
        }
    }

    private fun updateRecycleView() {
        val query: Query =
            if (DataService.INSTANCE.savedCats.size > 0) {
                FirebaseFirestore.getInstance().collection("cats")
                    .whereIn("catId", DataService.INSTANCE.savedCats.toList())
            } else {
                FirebaseFirestore.getInstance().collection("cats").whereIn("catId", listOf("-1"))
            }

        val options = FirestoreRecyclerOptions.Builder<Cat>().setQuery(query, Cat::class.java)
            .setLifecycleOwner(this).build()

        val adapter = object : FirestoreRecyclerAdapter<Cat, SavedCatCard>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCatCard {
                val localView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.saved_cat_card, parent, false)
                return SavedCatCard(localView)
            }

            override fun onBindViewHolder(holder: SavedCatCard, position: Int, model: Cat) {
                holder.bind(model)
            }
        }
        thisView.savedCatRecyclerView.adapter = adapter
    }

    override fun onObservedAdd(e: String) {
        updateRecycleView()
    }

    override fun onObservedRemove(e: String) {
        updateRecycleView()
    }
}
