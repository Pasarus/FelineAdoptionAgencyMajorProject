package uk.ac.aber.dcs.mmp.faa.ui.catinfo

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.ac.aber.dcs.mmp.faa.R

class CatCard : Fragment() {

    companion object {
        fun newInstance() = CatCard()
    }

    private lateinit var viewModel: CatCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cat_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CatCardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
