package com.example.ugd.Activity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.ugd.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UpdateDonasiTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(UpdateDonasi::class.java)

    @Test
    fun updateDonasiTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val appCompatButton = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editJudul),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    1
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("Banjir"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editDeskripsi),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    2
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("Hanyut"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.edittarget),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    3
                )
            )
        )
        appCompatEditText3.perform(scrollTo(), replaceText("1.000.000"), closeSoftKeyboard())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.editPenggalang),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scroll_view),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText4.perform(scrollTo(), replaceText("Devin Condro"), closeSoftKeyboard())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.cara),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editCaraPembayaran),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("Cash"), closeSoftKeyboard())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.daerah),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.editDaerah),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("Bali"), closeSoftKeyboard())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton7.perform(click())
        onView(isRoot()).perform(waitFor(3000))
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

    fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
