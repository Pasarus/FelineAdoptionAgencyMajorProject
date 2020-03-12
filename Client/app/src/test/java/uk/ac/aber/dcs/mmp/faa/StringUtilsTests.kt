package uk.ac.aber.dcs.mmp.faa

import junit.framework.Assert.assertEquals
import org.junit.Test
import uk.ac.aber.dcs.mmp.faa.utils.convertMonthsNumberToUsableString

class StringUtilsTests {
    @Test
    fun sixtyMonthsReturnsFiveYears() {
        assertEquals("5 Years Old", convertMonthsNumberToUsableString(60))
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
}