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

package uk.ac.aber.dcs.mmp.faa

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uk.ac.aber.dcs.mmp.faa.utils.ObserverOfStringSet
import uk.ac.aber.dcs.mmp.faa.utils.SimpleObservableStringSet

class SimpleObservableStringSetTests : ObserverOfStringSet {
    private var addCalledCount: Int = 0
    private var removeCalledCount: Int = 0
    private lateinit var testSet: SimpleObservableStringSet

    @Before
    fun setUp() {
        addCalledCount = 0
        removeCalledCount = 0
        testSet = SimpleObservableStringSet()
        testSet.addObserver(this)
    }

    override fun onObservedAdd(e: String) {
        addCalledCount += 1
    }

    override fun onObservedRemove(e: String) {
        removeCalledCount += 1
    }

    @Test
    fun test_notifiesObserversOnAdd() {
        testSet.add("test1")
        assertEquals(addCalledCount, 1)
    }

    @Test
    fun test_notifiesObserversOnAddOfDuplicate() {
        val test1 = "test1"
        testSet.add(test1)
        assertEquals(addCalledCount, 1)
        testSet.add(test1)
        assertEquals(addCalledCount, 2)
    }

    @Test
    fun test_notifiesObserversOnRemove() {
        val test1 = "test1"
        testSet.add(test1)
        testSet.remove(test1)
        assertEquals(addCalledCount, 1)
        assertEquals(removeCalledCount, 1)
    }

    @Test
    fun test_notifiesObserversOnRemoveOfNoneExistentString() {
        testSet.remove("test1")
        assertEquals(removeCalledCount, 1)
    }

    @Test
    fun test_notifiesMultipleTimesForMultipleAdds() {
        testSet.add("test1")
        assertEquals(addCalledCount, 1)
        testSet.add("test2")
        assertEquals(addCalledCount, 2)
        testSet.add("test3")
        assertEquals(addCalledCount, 3)
    }

    @Test
    fun test_notifiesMultipleTimesForMultipleRemoves() {
        val test1 = "test1"
        val test2 = "test2"
        val test3 = "test3"
        testSet.add(test1)
        assertEquals(addCalledCount, 1)
        testSet.add(test2)
        assertEquals(addCalledCount, 2)
        testSet.add(test3)
        assertEquals(addCalledCount, 3)
        testSet.remove(test1)
        assertEquals(removeCalledCount, 1)
        testSet.remove(test2)
        assertEquals(removeCalledCount, 2)
        testSet.remove(test3)
        assertEquals(removeCalledCount, 3)
    }

    @Test
    fun test_notifiesOncePerItemAddedWhenAddingMultipleItemsWithAddAll() {
        testSet.addAll(setOf("test1", "test2", "test3"))
        assertEquals(addCalledCount, 3)
    }

    @Test
    fun test_notifiesOncePerItemClearedWhenUsingClear() {
        testSet.addAll(setOf("test1", "test2", "test3"))
        assertEquals(addCalledCount, 3)

        testSet.clear()
        assertEquals(removeCalledCount, 3)
    }

    @Test
    fun test_notifiesOncePerItemClearedWhenRemovingMultipleItemsWithRemoveAll() {
        testSet.addAll(setOf("test1", "test2", "test3"))
        assertEquals(addCalledCount, 3)

        testSet.removeAll(setOf("test1", "test2", "test3"))
        assertEquals(removeCalledCount, 3)
    }

    // Tests below this point are to ensure that basic set functionality are still viable via this class
    @Test
    fun test_setSizeOption() {
        for (i in 1..10) {
            testSet.add("" + i)
            assertEquals(testSet.size, i)
        }
    }

    @Test
    fun test_setUniquenessIsGuarenteed() {
        val test1 = "test1"
        testSet.add(test1)
        assertEquals(testSet.size, 1)
        testSet.add(test1)
        assertEquals(testSet.size, 1)

        // Ensure the value in the set is test1
        for (wannabeTest1 in testSet) {
            assertEquals(wannabeTest1, test1)
        }
    }

    @Test
    fun test_setRetrievalViaIteratorStillWorks() {
        val test1 = "test1"
        testSet.add(test1)
        for (wannabeTest1 in testSet) {
            assertEquals(wannabeTest1, test1)
        }
    }

    @Test
    fun test_ensureUpdateOccursWithBlankFunction() {
        testSet.updateObserversAddBlank()
        assertEquals(addCalledCount, 1)
    }
}