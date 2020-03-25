package uk.ac.aber.dcs.mmp.faa.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.aber.dcs.mmp.faa.R
import uk.ac.aber.dcs.mmp.faa.utils.waitForFireStoreCollectionRequest

@LargeTest
@RunWith(AndroidJUnit4::class)
class AdoptionButtonIsPresentOnCatFragment {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun adoptionButtonIsPresentOnCatFragment() {
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

        waitForFireStoreCollectionRequest("cats")

        val materialCardView = onView(
            allOf(
                withId(R.id.catCard),
                childAtPosition(
                    allOf(
                        withId(R.id.catRecyclerView),
                        childAtPosition(
                            withClassName(`is`("android.widget.FrameLayout")),
                            0
                        )
                    ),
                    0
                )
            )
        )
        materialCardView.perform(click())

        onView(withId(R.id.catCardInfoFragmentScrollView)).perform(swipeUp())
        val button = onView(
            allOf(
                withId(R.id.catInfoFragmentAdoptButton),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed())).perform(click())
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
