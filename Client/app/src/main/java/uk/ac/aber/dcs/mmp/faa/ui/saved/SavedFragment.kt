package uk.ac.aber.dcs.mmp.faa.ui.saved

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.ac.aber.dcs.mmp.faa.R

class SavedFragment : Fragment() {

    companion object {
        fun newInstance() = SavedFragment()
    }

    private lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SavedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
