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

package uk.ac.aber.dcs.mmp.faa.utils

import com.google.firebase.firestore.FirebaseFirestore

fun waitForFireStoreCollectionRequest (collection: String){
    var isWaiting = true
    var countTime = 0
    val task = FirebaseFirestore.getInstance().collection(collection).get()
    task.addOnSuccessListener {
        isWaiting = false
    }
    task.addOnFailureListener {
        isWaiting = false
    }
    task.addOnCanceledListener {
        isWaiting = false
    }
    while (isWaiting || countTime > 1000) {
        Thread.sleep(20)
        countTime += 1
    }
}