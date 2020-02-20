package uk.ac.aber.dcs.mmp.faa.ui.adoptionstatus

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.ac.aber.dcs.mmp.faa.R

class AdoptionStatusCard : Fragment() {

    companion object {
        fun newInstance() = AdoptionStatusCard()
    }

    private lateinit var viewModel: AdoptionStatusCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adoption_status_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdoptionStatusCardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
