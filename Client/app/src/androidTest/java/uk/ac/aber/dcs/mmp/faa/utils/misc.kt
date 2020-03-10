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