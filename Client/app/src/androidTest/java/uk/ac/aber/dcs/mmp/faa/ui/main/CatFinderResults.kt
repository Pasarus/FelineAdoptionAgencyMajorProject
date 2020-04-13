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

package uk.ac.aber.dcs.mmp.faa.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.utils.waitForFireStoreCollectionRequest

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class CatFinderResults {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun catFinderResults() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.findCatFragment), withContentDescription("Cat Finder"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationBar),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        //This is where we add a hacky solution to ensure that FirebaseFirestore has had enough to
        // get the data by making the same request and getting the response.
        waitForFireStoreCollectionRequest("cats")

        val textView = onView(
            allOf(
                withId(R.id.catName), withText("Jaskier"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.catCard),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Jaskier")))

        val textView2 = onView(
            allOf(
                withId(R.id.catAge), withText("1 Year, 4 Months Old"),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("1 Year, 4 Months Old")))

        val textView3 = onView(
            allOf(
                withId(R.id.catLocation), withText("Borth Cattery"),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Borth Cattery")))

        val textView4 = onView(
            allOf(
                withId(R.id.catName), withText("Tuna"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.catCard),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Tuna")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
