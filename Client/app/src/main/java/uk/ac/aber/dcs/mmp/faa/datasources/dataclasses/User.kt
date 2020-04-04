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

package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.DocumentReference

@IgnoreExtraProperties
data class User(
    var addressLineOne: String? = "",
    var addressLineTwo: String? = "",
    var addressLineThree: String? = "",
    var county: String? = "",
    var name: String? = "",
    var mobileNumber: String? = "",
    var postCode: String? = "",
    var favouritedCats: List<String>? = ArrayList(),
    var adoptionProcesses: List<DocumentReference>? = ArrayList()
)