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

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(binding)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(binding)
    }

    // TODO: add End to End testing to the app
    @Test
    fun successfullyAddNewReminderScenarioTest() {
        val activity = ActivityScenario.launch(RemindersActivity::class.java)
        binding.monitorActivity(activity)

        // click on add new reminder fab
        onView(withId(R.id.addReminderFAB)).perform(click())

        // add new title
        onView(withId(R.id.reminderTitle)).perform(typeText("Testing title"))
        closeSoftKeyboard()

        // add new description
        onView(withId(R.id.reminderDescription)).perform(typeText("Testing description"))
        closeSoftKeyboard()

        // click on select reminder location
        onView(withId(R.id.selectLocation)).perform(click())

        // click on the mapLocation
        Thread.sleep(2000)
        onView(withId(R.id.map)).perform(longClick())

        // click on confirm button
        onView(withId(R.id.confirm)).perform(click())

        // click on save button
        onView(withId(R.id.saveReminder)).perform(click())

        // check if success toast appears
        onView(withId(R.id.reminderssRecyclerView)).check(
            matches(
                atPosition(
                    0,
                    withText("Testing title"),
                    R.id.title
                )
            )
        )

        activity.close()
    }
}
