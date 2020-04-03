package uk.ac.aber.dcs.mmp.faa.ui.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.settings_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)

        view.allNotificationsSwitch.setOnCheckedChangeListener {
                buttonView, isChecked ->
            if (isChecked){
                view.newCatNotification.isChecked = true
                view.newsNotification.isChecked = true
                view.adoptionStatusChangeNotifications.isChecked = true
            } else {
                view.newCatNotification.isChecked = false
                view.newsNotification.isChecked = false
                view.adoptionStatusChangeNotifications.isChecked = false
            }
        }

        view.newCatNotification.setOnCheckedChangeListener {
                buttonView, isChecked ->
            if (isChecked){

            } else {

            }
        }

        return view
    }

    private fun switchToDarkMode(){

    }

    private fun switchFromDarkMode(){

    }

    private fun turnOnAllNotifications() {

    }

    private fun turnOnAdoptionStatusChangeNotifications() {

    }

    private fun turnOnNewlyListedCatsNotifications() {

    }

    private fun turnOnAdoptionNewsNotifications() {

    }

}
