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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.mmp.faa.R

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class HelpFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun helpFragmentTest() {
        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatTextView = onView(
            allOf(
                withId(R.id.navDrawHelp), withText("Help"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navDrawerNavView),
                        1
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.title), withText("How to adopt a cat:")
            )
        )
        textView.check(matches(withText("How to adopt a cat:")))

        val textView2 = onView(
            allOf(
                withId(R.id.firstParagraph),
                withText("You need to find a cat in our helpful cat finder tool!, to do this please navigate to the cat finder tab using the bottom navigation bar.")
            )
        )
        textView2.check(matches(withText("You need to find a cat in our helpful cat finder tool!, to do this please navigate to the cat finder tab using the bottom navigation bar.")))

        val imageView = onView(
            allOf(
                withId(R.id.navBarImage)
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.secondParagraph),
                withText("Once using the tool, find a cat by browsing our selection, a cat will be displayed in a info card.")
            )
        )
        textView3.check(matches(withText("Once using the tool, find a cat by browsing our selection, a cat will be displayed in a info card.")))
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
