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
import org.junit.Test
import uk.ac.aber.dcs.mmp.faa.utils.boolToString
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString
import uk.ac.aber.dcs.mmp.faa.utils.stringToBool

class StringUtilsTests {
    @Test
    fun sixtyMonthsAndAboveReturnsFiveYears() {
        assertEquals("5 Years Old", convertMonthsNumberToUsableString(60))
        assertEquals("5 Years Old", convertMonthsNumberToUsableString(61))
    }

    @Test
    fun belowTwelveMonthsShowsOnlyMonths() {
        assertEquals("5 Months Old", convertMonthsNumberToUsableString(5))
    }

    @Test
    fun oneMonthShowsMonthInsteadOfMonths() {
        assertEquals("1 Month Old", convertMonthsNumberToUsableString(1))
    }

    @Test
    fun oneYearOldShowsOneYearOldInsteadOfOneYearsOld() {
        assertEquals("1 Year Old", convertMonthsNumberToUsableString(12))
    }

    @Test
    fun below60Above12ShowsYearsAndMonth() {
        assertEquals("1 Year, 6 Months Old", convertMonthsNumberToUsableString(18))
        assertEquals("4 Years, 11 Months Old", convertMonthsNumberToUsableString(59))
    }

    @Test
    fun boolToStringFalse() {
        assertEquals("false", boolToString(false))
    }

    @Test
    fun boolToStringTrue() {
        assertEquals("true", boolToString(true))
    }

    @Test
    fun stringToBoolTrue() {
        assertEquals(true, stringToBool("true"))
    }

    @Test
    fun stringToBoolFalse() {
        assertEquals(false, stringToBool("false"))
    }

    @Test
    fun stringToBoolRandom() {
        assertEquals(false, stringToBool("Random string here please"))
    }
}