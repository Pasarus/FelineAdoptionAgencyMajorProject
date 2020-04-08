package uk.ac.aber.dcs.mmp.faa.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Cat
import uk.ac.aber.dcs.mmp.faa.ui.main.MainActivity
import java.util.*
import kotlin.random.Random

class DatabaseListenerNotifier : Service() {

    private var catListener: ListenerRegistration? = null
    private var adoptionListener: ListenerRegistration? = null
    private val ADOPTION_STATUS_CHANGE_CHANNEL_ID = "ADOPTIONSTATUSCHANGECHANNELID"
    private val NEWLY_LISTED_CAT_CHANNEL_ID = "NEWLYLISTEDCATCHANNELID"
    private val ADOPTION_NEWS_CHANNEL_ID = "ADOPTIONNEWSCHANNELID"
    private var highestCatId: Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        FirebaseFirestore.getInstance().collection("cats").get().addOnSuccessListener { documents ->
            val allCats = documents.toObjects(Cat::class.java)
            for (cat in allCats) {
                if (highestCatId < cat.catId!!.toInt()) {
                    highestCatId = cat.catId!!.toInt()
                }
            }

            // With the highest cat id noted, now start the snapshot listeners
            startListeners()
        }
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startListeners() {
        // Start a firebase listener for cats
        catListener = FirebaseFirestore.getInstance().collection("cats")
            .addSnapshotListener { snapshots, e ->
                if (e == null) {
                    // No error so successful listen
                    for (dc in snapshots!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val cat: Cat = dc.document.toObject()
                            if (cat.catId?.toInt() ?: 0 > highestCatId) {
                                notifyOfNewCat(cat)
                                highestCatId = cat.catId!!.toInt()
                            }
                        }
                    }
                }
            }

        // Start Listener for current accounts adoption statuses
        val user = DataService.INSTANCE.user
        if (user != null) {
            adoptionListener = FirebaseFirestore.getInstance()
                .collection("adoptionProcesses").document(user.uid)
                .collection("adoptionProcesses").addSnapshotListener { snapshots, e ->
                    if (e == null) {
                        // No error so successful listen
                        for (dc in snapshots!!.documentChanges) {
                            if (dc.type == DocumentChange.Type.MODIFIED) {
                                val cat: Cat = dc.document.toObject()
                                if (cat.catId?.toInt() ?: 0 > highestCatId) {
                                    notifyOfAdoptionStatusChange(cat)
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun notifyOfAdoptionStatusChange(cat: Cat) {
        val catName = cat.catName

        // Setup the intent of the notification i.e. open the app
        val notifIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0)

        val builder = NotificationCompat.Builder(this, ADOPTION_STATUS_CHANGE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Adoption Status Change!")
            .setContentText("Please check your adoption status for the cat: $catName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(0, 99999), builder.build())
        }

    }

    private fun notifyOfNewCat(cat: Cat) {
        val catName = cat.catName

        // Setup the intent of the notification i.e. open the app
        val notifIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0)

        val builder = NotificationCompat.Builder(this, NEWLY_LISTED_CAT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Cat Listed!")
            .setContentText("We added a new cat: $catName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(0, 99999), builder.build())
        }

    }

    override fun onUnbind(intent: Intent?): Boolean {
        catListener?.remove()
        adoptionListener?.remove()
        return super.onUnbind(intent)
    }
}