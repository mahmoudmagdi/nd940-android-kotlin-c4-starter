package com.udacity.project4

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.atPosition
import com.udacity.project4.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@LargeTest
// END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed auto close @after method to close Koin after every test

    private lateinit var reminderDataSource: ReminderDataSource
    private lateinit var appContext: Application
    private val binding = DataBindingIdlingResource()

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(binding)
    }

    @Before
    fun init() {
        stopKoin() // stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }

        // declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }

        // Get our real repository
        reminderDataSource = get()

        // clear the data to start fresh
        runBlocking {
            reminderDataSource.deleteAllReminders()
        }
    }

    @After
    fun unregisterIdlingResource() = runBlocking {
        IdlingRegistry.getInstance().unregister(binding)

        reminderDataSource.deleteAllReminders()
    }

    // TODO: add End to End testing to the app
    @Test
    fun saveReminder() {
        // GIVEN - On the home screen
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        binding.monitorActivity(activityScenario)

        // WHEN - Add a new reminder
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(typeText("Title"))
        onView(withId(R.id.reminderDescription)).perform(typeText("Description"))
        onView(withId(R.id.selectLocation)).perform(click())
        onView(withId(R.id.map)).perform(longClick())
        onView(withId(R.id.confirm)).perform(click())
        closeSoftKeyboard()
        onView(withId(R.id.saveReminder)).perform(click())

        // THEN - Verify reminder is displayed on the screen
        onView(withId(R.id.reminderssRecyclerView)).check(
            matches(
                atPosition(
                    0,
                    withText("Title"),
                    R.id.title
                )
            )
        )
        onView(withId(R.id.reminderssRecyclerView)).check(
            matches(
                atPosition(
                    0,
                    withText("Description"),
                    R.id.description
                )
            )
        )

        // Clean up
        activityScenario.close()
    }

    @Test
    fun saveReminder_noTitle() {
        // GIVEN - On the home screen
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        binding.monitorActivity(activityScenario)

        // WHEN - Add a new reminder
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderDescription)).perform(typeText("Description"))
        onView(withId(R.id.selectLocation)).perform(click())
        onView(withId(R.id.map)).perform(longClick())
        onView(withId(R.id.confirm)).perform(click())
        closeSoftKeyboard()
        onView(withId(R.id.saveReminder)).perform(click())

        // THEN - Verify reminder is displayed on the screen
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.err_enter_title)))

        // Clean up
        activityScenario.close()
    }

    @Test
    fun saveReminder_noLocation() {
        // GIVEN - On the home screen
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        binding.monitorActivity(activityScenario)

        // WHEN - Add a new reminder
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(typeText("Title"))
        onView(withId(R.id.reminderDescription)).perform(typeText("Description"))
        closeSoftKeyboard()
        onView(withId(R.id.saveReminder)).perform(click())

        // THEN - Verify reminder is displayed on the screen
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.err_select_location)))

        // Clean up
        activityScenario.close()
    }
}
