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

class SimpleObservableStringSet : HashSet<String> {

    private var observers: ArrayList<ObserverOfStringSet> = ArrayList()

    constructor() : super()
    constructor(set: Set<String>) : super(set)

    override fun add(element: String): Boolean {
        val returnValue = super.add(element)
        // Not always true, as when a Set is being constructed it may call this method
        observers.forEach { it.onObservedAdd(element) }
        return returnValue
    }

    override fun remove(element: String): Boolean {
        val returnValue = super.remove(element)
        // Not always true, as when a Set is being constructed it may call this method
        observers.forEach { it.onObservedRemove(element) }
        return returnValue
    }

    override fun removeAll(elements: Collection<String>): Boolean {
        val returnValue = super.removeAll(elements)
        // Not always true, as when a Set is being constructed it may call this method
        observers.forEach {
            val it2 = it
            elements.forEach { it2.onObservedRemove(it) }
        }
        return returnValue
    }

    override fun clear() {
        val copyOfClearedItems = HashSet<String>()
        copyOfClearedItems.addAll(this)
        // Not always true, as when a Set is being constructed it may call this method
        observers.forEach {
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

    fun updateObserversAddBlank() {
        observers.forEach { it.onObservedAdd("") }
    }
}
