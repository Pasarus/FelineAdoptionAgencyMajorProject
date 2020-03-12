package uk.ac.aber.dcs.mmp.faa.utils

fun convertMonthsNumberToUsableString(months: Int?): String {
    val years = months!! / 12
    val finalMonths = months % 12
    return when {
        finalMonths == 0 -> {
            if (years == 1) {
                "$years Year Old"
            } else {
                "$years Years Old"
            }
        }
        years in 1..4 -> {
            if (years == 1) {
                "$years Year, $finalMonths Months Old"
            } else {
                "$years Years, $finalMonths Months Old"
            }
        }
        years > 5 -> {
            // If age is > 5 years
            "$years Years Old"
        }
        else -> {
            if (months == 1) {
                "$months Month Old"
            } else {
                "$months Months Old"
            }
        }
    }
}