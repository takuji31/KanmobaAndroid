package jp.takuji31.kanmoba.num24

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import jp.takuji31.kanmoba.R
import jp.takuji31.kanmoba.model.Artist
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IdlingResourceActivityTest {
    @JvmField
    @Rule
    val rule : ActivityTestRule<IdlingResourceActivity> = ActivityTestRule(IdlingResourceActivity::class.java, true, true)

    var initializeIdlingResource : IdlingResource? = null

    @Before
    fun setUp() {
        initializeIdlingResource = rule.activity.viewModel.loadingIdlingResource
        IdlingRegistry.getInstance().register(initializeIdlingResource)
    }

    @After
    fun tearDown() {
        initializeIdlingResource?.run {
            IdlingRegistry.getInstance().unregister(this)
            initializeIdlingResource = null
        }
    }

    @Test
    fun testInitialize() {
        onView(withId(R.id.recyclerView)).check { view, noMatchingViewException ->
            val recyclerView = view as RecyclerView
            assertEquals(recyclerView.childCount, Artist.list.size)
            assertEquals(recyclerView.adapter.itemCount, Artist.list.size)

        }
    }

    @Test
    fun testReload() {

        // remove 3 items
        repeat(3) {
            openActionBarOverflowOrOptionsMenu(rule.activity)
            onView(withText("Remove first")).perform(click())
        }

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())

        onView(withId(R.id.recyclerView)).check { view, noMatchingViewException ->
            val recyclerView = view as RecyclerView
            assertEquals(recyclerView.childCount, Artist.list.size)
            assertEquals(recyclerView.adapter.itemCount, Artist.list.size)

        }
    }
}