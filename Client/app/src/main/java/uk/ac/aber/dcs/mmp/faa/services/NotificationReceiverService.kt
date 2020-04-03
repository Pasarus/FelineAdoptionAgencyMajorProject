package uk.ac.aber.dcs.mmp.faa.services

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.auth.Token
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import uk.ac.aber.dcs.mmp.faa.R

class NotificationReceiverService: FirebaseMessagingService() {

    lateinit var token: String

    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                // Get new Instance ID token
                token = task.result?.token.toString()
            }
    }

    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }
}