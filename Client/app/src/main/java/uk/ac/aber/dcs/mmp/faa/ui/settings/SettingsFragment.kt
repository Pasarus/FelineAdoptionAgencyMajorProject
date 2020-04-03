package uk.ac.aber.dcs.mmp.faa.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.settings_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)

        // Setup UI for darkmode
        if (DataService.INSTANCE.darkMode){
            view.darkModeLayout.setBackgroundColor(view.resources.getColor(R.color.darkCardBackground, null))
            view.darkModeText.setTextColor(view.resources.getColor(R.color.white, null))
        }


        // Ensure settings reflects current state.
        if (DataService.INSTANCE.settings[DataService.INSTANCE.DARK_MODE_KEY]!!){
            view.darkModeSwitch.isChecked = true
        }

        view.darkModeSwitch.setOnCheckedChangeListener{
            buttonView, isChecked ->
            if (isChecked){
                // Switch to dark mode
                DataService.INSTANCE.darkMode()
            } else {
                // Switch to light mode
                DataService.INSTANCE.lightMode()
            }
            DataService.INSTANCE.settings[DataService.INSTANCE.DARK_MODE_KEY] = isChecked
        }

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
