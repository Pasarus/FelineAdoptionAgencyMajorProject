package uk.ac.aber.dcs.mmp.faa.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.saved_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.utils.ObserverOfStringSet

class SavedFragment : Fragment(), ObserverOfStringSet {

    private lateinit var thisView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.saved_fragment, container, false)

        if (DataService.INSTANCE.user != null) {
            view.savedCatRecyclerView.visibility = View.VISIBLE
            view.savedCatNotSignedIn.visibility = View.INVISIBLE
        } else {
            view.savedCatRecyclerView.visibility = View.INVISIBLE
            view.savedCatNotSignedIn.visibility = View.VISIBLE
        }
        thisView = view

        DataService.INSTANCE.savedCats.addObserver(this)

        return view
    }

    override fun onStart() {
        super.onStart()
        updateRecycleView()
    }

    private fun updateRecycleView() {
        val query: Query =
            if (DataService.INSTANCE.savedCats.size > 0){
                FirebaseFirestore.getInstance().collection("cats").whereIn("catId", DataService.INSTANCE.savedCats.toList())
            } else {
                FirebaseFirestore.getInstance().collection("cats").whereIn("catId", listOf("-1"))
            }

        val options = FirestoreRecyclerOptions.Builder<Cat>().setQuery(query, Cat::class.java).setLifecycleOwner(this).build()

        val adapter = object : FirestoreRecyclerAdapter<Cat, SavedCatCard>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCatCard {
                val localView = LayoutInflater.from(parent.context).inflate(R.layout.saved_cat_card, parent, false)
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
