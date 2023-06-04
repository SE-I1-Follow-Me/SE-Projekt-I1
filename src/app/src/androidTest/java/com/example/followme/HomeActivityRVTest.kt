package com.example.followme


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityRVTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<HomeActivity> = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testRecyclerViewItemCount() {
        var itemCount = 0
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                itemCount = HomeActivity.adapter.itemCount
            }
        }
        onView(withId(R.id.rvRobots))  // replace with your RecyclerView id
            .check(matches(RecyclerViewMatcher.withItemCount(itemCount)))
    }



}
