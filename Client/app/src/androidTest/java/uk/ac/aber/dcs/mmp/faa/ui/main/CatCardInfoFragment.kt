package uk.ac.aber.dcs.mmp.faa.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
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
import uk.ac.aber.dcs.mmp.faa.utils.waitForFireStoreCollectionRequest


@LargeTest
@RunWith(AndroidJUnit4::class)
class CatCardInfoFragment {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun catCardInfoFragment() {
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
                ),
                isDisplayed()
            )
        )
        materialCardView.perform(click())

        waitForFireStoreCollectionRequest("cats")

        val textView = onView(
            allOf(
                withId(R.id.catInfoFragmentCatName), withText("Jaskier")

            )
        )
        textView.check(matches(withText("Jaskier")))

        val textView2 = onView(
            allOf(
                withId(R.id.catInfoFragmentCatAge), withText("1 Year, 4 Months Old")

            )
        )
        textView2.check(matches(withText("1 Year, 4 Months Old")))

        val textView3 = onView(
            allOf(
                withId(R.id.catInfoFragmentCatLocation), withText("Aberystwyth Cattery")
            )
        )
        textView3.check(matches(withText("Aberystwyth Cattery")))

        val imageView = onView(
            allOf(
                withId(R.id.catInfoFragmentImage), withContentDescription("The Cat"),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView4 = onView(
            allOf(
                withId(R.id.catInfoFragmentSex), withText("Male")
            )
        )
        textView4.check(matches(withText("Male")))

        val textView5 = onView(
            allOf(
                withId(R.id.catInfoFragmentYoungKids), withText("Can be around young children")

            )
        )
        textView5.check(matches(withText("Can be around young children")))

        val textView6 = onView(
            allOf(
                withId(R.id.catInfoFragmentPrimaryAgeKids),
                withText("Can be around primary school aged kids")

            )
        )
        textView6.check(matches(withText("Can be around primary school aged kids")))

        val textView7 = onView(
            allOf(
                withId(R.id.catInfoFragmentHighSchoolKids),
                withText("Can be around High School children")

            )
        )
        textView7.check(matches(withText("Can be around High School children")))

        val textView8 = onView(
            allOf(
                withId(R.id.catInfoFragmentCanDealWithCats), withText("Can be with other cats")

            )
        )
        textView8.check(matches(withText("Can be with other cats")))

        val textView9 = onView(
            allOf(
                withId(R.id.catInfoFragmentCanDealWithDogs), withText("Cannot be around dogs")

            )
        )
        textView9.check(matches(withText("Cannot be around dogs")))

        val textView10 = onView(
            allOf(
                withId(R.id.catInfoFragmentCanDealInside), withText("Indoor Only Cat")
            )
        )
        textView10.check(matches(withText("Indoor Only Cat")))


        val textView11 = onView(
            allOf(
                withId(R.id.catInfoFragmentNeutered), withText("This cat is Neutered")
            )
        )
        textView11.check(matches(withText("This cat is Neutered")))

        val textView12 = onView(
            allOf(
                withId(R.id.catInfoFragmentDisabled), withText("This cat is not disabled")
            )
        )
        textView12.check(matches(withText("This cat is not disabled")))
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