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
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.mmp.faa.R

/**
 * This is a generated file using Espresso Test Recorder
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class BottomNavTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun bottomNavTest() {
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

        val textView = onView(
            allOf(
                withText("findCatFragment"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("findCatFragment")))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.savedFragment), withContentDescription("Saved Cats"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationBar),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val textView2 = onView(
            allOf(
                withText("savedCatFragment"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("savedCatFragment")))

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.homeFragment), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationBar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val textView3 = onView(
            allOf(
                withText("homeFragment"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("homeFragment")))
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
