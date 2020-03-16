package uk.ac.aber.dcs.mmp.faa.ui.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.user_data.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

class MyAccountFragment : Fragment() {

    companion object {
        fun newInstance() = MyAccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_account_fragment, container, false)

        DataService.INSTANCE.getDataForViewFromFirestore(view)
        view.updateAccountInformationButton.setOnClickListener {
            // Grab all the data
            DataService.INSTANCE.updateUserDataForView(view)
        }

        return view
    }


}
