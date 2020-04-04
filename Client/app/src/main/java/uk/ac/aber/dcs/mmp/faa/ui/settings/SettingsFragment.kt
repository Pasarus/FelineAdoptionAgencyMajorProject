/*   Copyright 2020 Samuel Jones
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.aber.dcs.mmp.faa.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        if (DataService.INSTANCE.darkMode) {
            view.darkModeLayout.setBackgroundColor(
                view.resources.getColor(
                    R.color.darkCardBackground,
                    null
                )
            )
            view.darkModeText.setTextColor(view.resources.getColor(R.color.white, null))
        }


        // Ensure settings reflects current state.
        if (DataService.INSTANCE.settings[DataService.INSTANCE.DARK_MODE_KEY]!!) {
            view.darkModeSwitch.isChecked = true
        }

        view.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch to dark mode
                DataService.INSTANCE.darkMode()
            } else {
                // Switch to light mode
                DataService.INSTANCE.lightMode()
            }
            DataService.INSTANCE.settings[DataService.INSTANCE.DARK_MODE_KEY] = isChecked
        }

        view.allNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                view.newCatNotification.isChecked = true
                view.newsNotification.isChecked = true
                view.adoptionStatusChangeNotifications.isChecked = true
            } else {
                view.newCatNotification.isChecked = false
                view.newsNotification.isChecked = false
                view.adoptionStatusChangeNotifications.isChecked = false
            }
        }

        view.newCatNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
        }

        return view
    }

    private fun switchToDarkMode() {

    }

    private fun switchFromDarkMode() {

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
