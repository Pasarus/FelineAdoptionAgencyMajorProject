package uk.ac.aber.dcs.mmp.faa.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.home_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.AdoptionProcess
import uk.ac.aber.dcs.mmp.faa.ui.adoption.AdoptionStatusCard

class HomeFragment : Fragment() {
    lateinit var layoutView: View

    private var query = FirebaseFirestore.getInstance().collection("adoptionProcesses").document("default").collection("adoptionProcesses").limit(10)

    private val options = FirestoreRecyclerOptions.Builder<AdoptionProcess>().setQuery(query, AdoptionProcess::class.java).setLifecycleOwner(this).build()

    private val adapter = object : FirestoreRecyclerAdapter<AdoptionProcess, AdoptionStatusCard>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionStatusCard {
            val localView = LayoutInflater.from(parent.context).inflate(R.layout.adoption_status_card, parent, false)
            return AdoptionStatusCard(localView)
        }

        override fun onBindViewHolder(holder: AdoptionStatusCard, position: Int, model: AdoptionProcess) {
            holder.bind(model)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        view.adoptionStatusRecyclerView.adapter = adapter
        layoutView = view
        return view
    }

    override fun onResume() {
        super.onResume()
        val user = DataService.INSTANCE.user
        val reyclerView = layoutView.findViewById<RecyclerView>(R.id.adoptionStatusRecyclerView)
        val noUserText = layoutView.findViewById<TextView>(R.id.noUserYetTextView)
        if (user != null){
            // Hide text and setup recycler view as the user is signed in.
            reyclerView.visibility = VISIBLE
            noUserText.visibility = INVISIBLE
            val query = FirebaseFirestore.getInstance().collection("adoptionProcesses").document(user.uid).collection("adoptionProcesses").limit(10)

            val options = FirestoreRecyclerOptions.Builder<AdoptionProcess>().setQuery(query, AdoptionProcess::class.java).setLifecycleOwner(this).build()

            val adapter = object : FirestoreRecyclerAdapter<AdoptionProcess, AdoptionStatusCard>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionStatusCard {
                    val localView = LayoutInflater.from(parent.context).inflate(R.layout.adoption_status_card, parent, false)
                    return AdoptionStatusCard(localView)
                }

                override fun onBindViewHolder(holder: AdoptionStatusCard, position: Int, model: AdoptionProcess) {
                    holder.bind(model)
                }
            }
            reyclerView.adapter = adapter
        } else {
            reyclerView.visibility = INVISIBLE
            noUserText.visibility = VISIBLE
        }
        // Else leave it showing nothing
    }

}
