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
import androidx.test.espresso.action.ViewActions.*
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
class FeedbackFormTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun feedbackFormTest() {
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
                withId(R.id.navDrawFeedback), withText("Feedback"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navDrawerNavView),
                        1
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.feedbackOpeningParagraph),
                withText("You will be asked to login to the application if not already done so when you click send, this is to reduce spam, and allow responses from developers, to your emails."),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("You will be asked to login to the application if not already done so when you click send, this is to reduce spam, and allow responses from developers, to your emails.")))

        val imageView = onView(
            allOf(
                withId(R.id.feedbackIcon),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val editText = onView(
                withId(R.id.feedback)

        )
        editText.check(matches(isDisplayed()))

        val checkBox = onView(
            allOf(
                withId(R.id.reply),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        checkBox.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.developerContactText),
                withText("Would you like to be replied to, by the developer?"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Would you like to be replied to, by the developer?")))

        val button = onView(
            allOf(
                withId(R.id.sendFeedbackButton),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val textInputEditText = onView(
            allOf(
                withId(R.id.feedback),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("Feedback text test"), closeSoftKeyboard())

        val editText3 = onView(
            allOf(
                withId(R.id.feedback), withText("Feedback text test"),
                isDisplayed()
            )
        )
        editText3.check(matches(withText("Feedback text test")))
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
