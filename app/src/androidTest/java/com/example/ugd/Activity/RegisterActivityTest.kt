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
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val appCompatButton = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.etUsername),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.etUsername),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("Condro"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.etPassword),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("Condro"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.etEmail),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("Condro@gmail.com"), closeSoftKeyboard())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.etTanggal),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("05/07/2002"), closeSoftKeyboard())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.etPhone),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("082354673065"), closeSoftKeyboard())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        appCompatButton6.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.etPhone),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        textInputEditText7.perform(replaceText("082354673065"), closeSoftKeyboard())

        val appCompatButton7 = onView(
            allOf(
                withId(R.id.btnSignUp), withText("Sign Up"),
                childAtPosition(
                    allOf(
                        withId(R.id.registerLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    10
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
