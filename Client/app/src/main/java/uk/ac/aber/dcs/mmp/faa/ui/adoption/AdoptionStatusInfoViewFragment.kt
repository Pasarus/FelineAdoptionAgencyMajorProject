package uk.ac.aber.dcs.mmp.faa.ui.adoption

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.ac.aber.dcs.mmp.faa.R

class AdoptionStatusInfoViewFragment : Fragment() {

    companion object {
        fun newInstance() = AdoptionStatusInfoViewFragment()
    }

    private lateinit var viewModel: AdoptionStatusInfoViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adoption_status_info_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdoptionStatusInfoViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
