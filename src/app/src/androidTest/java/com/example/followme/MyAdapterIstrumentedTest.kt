package com.example.followme

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyAdapterInstrumentedTest {

    //activity scenario
    @get:Rule
    var activityRule: ActivityScenarioRule<HomeActivity> = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testRecyclerViewItemDisplay() {
        onView(withText("Roboter A1")).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewItemClick() {
        onView(withText("Roboter A1")).perform(click())
        //TODO: verify change after click
    }
}
