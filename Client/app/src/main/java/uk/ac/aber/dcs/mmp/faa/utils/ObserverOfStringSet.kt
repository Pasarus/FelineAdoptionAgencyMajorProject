package uk.ac.aber.dcs.mmp.faa.utils

interface ObserverOfStringSet {
    fun onObservedAdd(e: String)
    fun onObservedRemove(e: String)
}