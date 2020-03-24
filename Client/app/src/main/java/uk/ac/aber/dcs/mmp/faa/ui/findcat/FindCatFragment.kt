package uk.ac.aber.dcs.mmp.faa.ui.findcat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.find_cat_fragment.*
import kotlinx.android.synthetic.main.find_cat_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.ui.catinfo.CatCard

class FindCatFragment : Fragment() {

    private val query = FirebaseFirestore.getInstance().collection("cats").limit(50)

    private val options = FirestoreRecyclerOptions.Builder<Cat>().setQuery(query, Cat::class.java).setLifecycleOwner(this).build()

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
