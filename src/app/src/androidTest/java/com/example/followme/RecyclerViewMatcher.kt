package com.example.followme

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object RecyclerViewMatcher {

    fun withItemCount(expectedCount: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var actualCount = 0

            override fun matchesSafely(view: View): Boolean {
                if (view !is RecyclerView) return false
                actualCount = view.adapter?.itemCount ?: 0
                return actualCount == expectedCount
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView with item count: $expectedCount")
                if (actualCount != -1) {
                    description.appendText("\n[Current item count: $actualCount]")
                }
            }
        }
    }
}
