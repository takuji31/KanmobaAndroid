package jp.takuji31.kanmoba.num24

import android.app.Activity
import android.app.Instrumentation
import android.app.SearchManager
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import jp.takuji31.kanmoba.R
import jp.takuji31.kanmoba.model.Artist
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IdlingResourceActivityTest {
    @JvmField
    @Rule
    val rule : IntentsTestRule<IdlingResourceActivity> = IntentsTestRule(IdlingResourceActivity::class.java, true, true)

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

    @Test
    fun testOpenSearchLink() {
        intending(hasAction(Intent.ACTION_WEB_SEARCH)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withText("小倉唯")).perform(click())
        intended(allOf(
                hasAction(Intent.ACTION_WEB_SEARCH),
                hasExtra(SearchManager.QUERY, "小倉唯")
        ))
    }
}