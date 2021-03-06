package uk.ac.aber.dcs.mmp.faa.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
@RunWith(AndroidJUnit4::class)
class AboutFragmentContents {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun aboutFragmentContents1() {
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

        val materialTextView = onView(
            allOf(
                withId(R.id.navDrawAbout), withText("About"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navDrawerNavView),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val textView = onView(
            allOf(
                withText("This application is meant as a demonstration of how the adoption of a cat could be fully integrated into an app via a backend database and user verification system. This mobile application was created under an Apache 2.0 License (https://www.apache.org/licenses/LICENSE-2.0)"),
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
        textView.check(matches(withText("This application is meant as a demonstration of how the adoption of a cat could be fully integrated into an app via a backend database and user verification system. This mobile application was created under an Apache 2.0 License (https://www.apache.org/licenses/LICENSE-2.0)")))

        val textView2 = onView(
            allOf(
                withText("Whilst use of this applications source code is free to everyone, the explicit copyright is owned by the sole author Samuel Jones (samjones714@gmail.com)"),
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
        textView2.check(matches(withText("Whilst use of this applications source code is free to everyone, the explicit copyright is owned by the sole author Samuel Jones (samjones714@gmail.com)")))

        val textView3 = onView(
            allOf(
                withText("It is distributed open source at https://github.com/Pasarus/FelineAdoptionAgencyMajorProject"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navHostFragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("It is distributed open source at https://github.com/Pasarus/FelineAdoptionAgencyMajorProject")))

        val textView4 = onView(
            allOf(
                withText("As part of this application certain icons have been used, these Material Design based icon are created by the community and are distributed under SIL Open Font License 1.1 (https://opensource.org/licenses/OFL-1.1)."),
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
        textView4.check(matches(withText("As part of this application certain icons have been used, these Material Design based icon are created by the community and are distributed under SIL Open Font License 1.1 (https://opensource.org/licenses/OFL-1.1).")))

        val textView5 = onView(
            allOf(
                withText("The icons created by google as part of Material Design are distributed under an Apache 2.0 License (http://www.apache.org/licenses/LICENSE-2.0)."),
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
        textView5.check(matches(withText("The icons created by google as part of Material Design are distributed under an Apache 2.0 License (http://www.apache.org/licenses/LICENSE-2.0).")))
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
