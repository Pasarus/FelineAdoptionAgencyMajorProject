package uk.ac.aber.dcs.mmp.faa.datasources.dataclasses

import com.google.firebase.firestore.DocumentReference
import org.w3c.dom.Document

class Feedback {

    var feedback: String? = null
    var developerReplyRequested: Boolean? = null
    var date: String? = null
    var userDocument: DocumentReference? = null

    constructor() {}
    constructor(feedback: String, devReplyRequested: Boolean, date: String, userDocument: DocumentReference){
        this.feedback = feedback
        this.developerReplyRequested = devReplyRequested
        this.date = date
        this.userDocument = userDocument
    }
}