package uk.ac.aber.dcs.mmp.faa.utils

public fun convertMonthsNumberToUsableString(months: Int?): String {
    return when {
        months!! > 12 && months < 60 -> {
            val years = months / 12
            val finalMonths = months % 12
            if (years == 1) {
                "$years Year, $finalMonths Months Old"
            } else {
                "$years Years, $finalMonths Months Old"
            }
        }
        months > 60 -> {
            // If age is > 5 years
            val years = months / 12
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