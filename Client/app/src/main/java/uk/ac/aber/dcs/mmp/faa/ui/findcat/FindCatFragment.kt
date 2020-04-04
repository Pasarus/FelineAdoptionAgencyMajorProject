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
import androidx.fragment.app.Fragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.find_cat_fragment.view.*
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.ui.catinfo.CatCard

class FindCatFragment : Fragment() {

    private val query = FirebaseFirestore.getInstance().collection("cats").limit(50)

    private val options = FirestoreRecyclerOptions.Builder<Cat>().setQuery(query, Cat::class.java)
        .setLifecycleOwner(this).build()

    private val adapter = object : FirestoreRecyclerAdapter<Cat, CatCard>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatCard {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.cat_card, parent, false)
            return CatCard(view)
        }

        override fun onBindViewHolder(holder: CatCard, position: Int, model: Cat) {
            holder.bind(model)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_cat_fragment, container, false)

        view.catRecyclerView.adapter = adapter

        return view
    }
}
