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

/**
 * Converts a passed integer to a string that is used to represent the age of a cat in words. If
 * the cat is over 5 years old just return years, under 5 years but over 1, return both, exactly
 * 1, 2, 3, 4 years old then return only years, and finally if if below 1 year old return only
 * months.
 */
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
        years >= 5 -> {
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