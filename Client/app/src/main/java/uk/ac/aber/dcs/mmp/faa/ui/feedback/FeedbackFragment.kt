package uk.ac.aber.dcs.mmp.faa.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.feedback_fragment.view.*

import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.datasources.DataService
import uk.ac.aber.dcs.mmp.faa.datasources.dataclasses.Feedback
import java.util.*

class FeedbackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.feedback_fragment, container, false)

        view.sendFeedbackButton.setOnClickListener {
            if (DataService.INSTANCE.user == null) {
                DataService.INSTANCE.mainActivity.doLogin()
            } else {
                val feedback = view.feedback.text.toString()
                val devReply = view.reply.isChecked
                val userUid = DataService.INSTANCE.user!!.uid
                val userRef = FirebaseFirestore.getInstance().collection("users")
                    .document(userUid)

                val time = Calendar.getInstance().time.toString()
                val feedbackId =  "$userUid-$time"
                val feedbackData = Feedback(feedback, devReply, time, userRef)

                FirebaseFirestore.getInstance().collection("feedback")
                    .document(feedbackId).set(feedbackData).addOnSuccessListener {
                        findNavController().navigate(R.id.homeFragment)
                    }
            }
        }

        return view
    }

}
