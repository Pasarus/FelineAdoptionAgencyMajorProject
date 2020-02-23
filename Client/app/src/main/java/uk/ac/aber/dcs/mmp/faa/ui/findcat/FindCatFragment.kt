package uk.ac.aber.dcs.mmp.faa.ui.findcat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.ac.aber.dcs.mmp.faa.R

class FindCatFragment : Fragment() {

    companion object {
        fun newInstance() = FindCatFragment()
    }

    private lateinit var viewModel: FindCatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.find_cat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FindCatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
