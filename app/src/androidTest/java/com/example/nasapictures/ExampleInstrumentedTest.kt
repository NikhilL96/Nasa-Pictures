package com.example.nasapictures

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nasapictures.view.activity.ImageDetailsActivity
import com.example.nasapictures.view.activity.ImagesGridActivity
import com.example.nasapictures.view.adapter.ImageCarouselAdapter
import com.example.nasapictures.view.adapter.ImagesGridAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun happyPath() {
        ActivityScenario.launch(ImagesGridActivity::class.java)

        // Check if items are present
        Espresso.onView(withId(R.id.image_list_recycler_view))
            .check(RecyclerViewItemCountAssertion(1))

        Espresso.onView(withId(R.id.image_list_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ImagesGridAdapter.ImagesGridViewHolder>(1, ViewActions.click()))
    }

    @Test
    fun fullPath() {
        ActivityScenario.launch(ImagesGridActivity::class.java)

        // Check if items are present
        Espresso.onView(withId(R.id.image_list_recycler_view))
            .check(RecyclerViewItemCountAssertion(20))

        //check clicking on an image for first 10 items
        for (index in 0 until 10) {
            Espresso.onView(withId(R.id.image_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ImagesGridAdapter.ImagesGridViewHolder>(index, ViewActions.click()))
            Espresso.onView(withId(R.id.image_count_tv)).check(matches(withText("${index+1} / 26")))
            Espresso.onView(withId(R.id.exit_image_details_button)).perform(ViewActions.click())
        }

        Espresso.onView(withId(R.id.image_list_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ImagesGridAdapter.ImagesGridViewHolder>(10, ViewActions.click()))

        //check scrolling on images for next 10 items
        for (index in 10 until 20) {
            Espresso.onView(withId(R.id.image_count_tv)).check(matches(withText("${index+1} / 26")))
            Espresso.onView(withId(R.id.image_carousel_recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<ImageCarouselAdapter.ImageCarouselViewHolder>(index+1))
        }

    }
}

class RecyclerViewItemCountAssertion(private val minimumCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as? RecyclerView
        val adapter = recyclerView?.adapter
        MatcherAssert.assertThat("images grid has atleast one item", adapter?.itemCount?:0 >= minimumCount)
    }
}