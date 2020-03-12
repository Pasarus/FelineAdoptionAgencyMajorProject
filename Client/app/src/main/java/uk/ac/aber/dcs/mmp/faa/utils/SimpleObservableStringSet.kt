package uk.ac.aber.dcs.mmp.faa.utils

class SimpleObservableStringSet : HashSet<String> {

    private var observers: ArrayList<ObserverOfStringSet> = ArrayList()

    constructor(): super()
    constructor(set: Set<String>): super(set)

    override fun add(element: String): Boolean {
        val returnValue = super.add(element)
        // Not always true, as when a Set is being constructed it may call this method
        if (observers != null)
            observers.forEach { it.onObservedAdd(element) }
        return returnValue
    }

    override fun remove(element: String): Boolean {
        val returnValue = super.remove(element)
        // Not always true, as when a Set is being constructed it may call this method
        if (observers != null)
            observers.forEach { it.onObservedRemove(element) }
        return returnValue
    }

    override fun removeAll(elements: Collection<String>): Boolean {
        val returnValue = super.removeAll(elements)
        // Not always true, as when a Set is being constructed it may call this method
        if (observers != null)
            observers.forEach{
            val it2 = it
            elements.forEach { it2.onObservedRemove(it) }
            }
        return returnValue
    }

    override fun clear() {
        val copyOfClearedItems = HashSet<String>()
        copyOfClearedItems.addAll(this)
        // Not always true, as when a Set is being constructed it may call this method
        if (observers != null)
            observers.forEach{
                val it2 = it
                copyOfClearedItems.forEach { it2.onObservedRemove(it) }
            }
        super.clear()
    }

    fun addObserver(observer: ObserverOfStringSet) {
        observers.add(observer)
    }

    fun removeObserver(observer: ObserverOfStringSet) {
        observers.remove(observer)
    }
}
