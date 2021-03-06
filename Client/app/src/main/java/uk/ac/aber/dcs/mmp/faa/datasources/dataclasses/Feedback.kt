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

import com.google.firebase.firestore.DocumentReference

/**
 * This is a data class that is based entirely on the formation of data in the google firebase
 * firestore. It is equal to that of the firestore data files inside of the collection named
 * "feedback". This is not at current expected to allow data retrieval but for pushing data in the
 * correct format to the firestore.
 */
class Feedback {

    var feedback: String? = null
    var developerReplyRequested: Boolean? = null
    var date: String? = null
    var userDocument: DocumentReference? = null

    constructor() {}
    constructor(
        feedback: String,
        devReplyRequested: Boolean,
        date: String,
        userDocument: DocumentReference
    ) {
        this.feedback = feedback
        this.developerReplyRequested = devReplyRequested
        this.date = date
        this.userDocument = userDocument
    }
}