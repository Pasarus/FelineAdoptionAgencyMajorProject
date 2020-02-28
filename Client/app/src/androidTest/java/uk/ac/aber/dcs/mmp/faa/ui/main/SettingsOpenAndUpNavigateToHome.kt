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

@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsOpenAndUpNavigateToHome {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun settingsOpenAndUpNavigateToHome() {
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.actionSettingsButton), withContentDescription("Settings"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val imageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val imageButton2 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))
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
