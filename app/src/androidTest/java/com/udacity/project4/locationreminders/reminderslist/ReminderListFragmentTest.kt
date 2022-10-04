package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

//    COMPLETED-TODO: test the navigation of the fragments.
//    COMPLETED-TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    private lateinit var reminderDataSource: FakeDataSource
    private lateinit var remindersListViewModel: RemindersListViewModel

    private val _binding = DataBindingIdlingResource()

    @Before
    fun createDbAndRepo() {
        reminderDataSource = FakeDataSource()
        remindersListViewModel =
            RemindersListViewModel(getApplicationContext(), reminderDataSource)

        stopKoin()
        modules()
    }

    private fun modules() {
        val mModule = module {
            single {
                remindersListViewModel
            }
        }
        startKoin {
            modules(listOf(mModule))
        }
    }

    @Test
    fun test_the_navigation_of_the_fragments() {
        val fragment = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        _binding.monitorFragment(fragment)

        val navController = mock(NavController::class.java)
        fragment.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.addReminderFAB)).perform(click())
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }

    @Test
    fun test_the_displayed_data_on_the_UI() {
        GlobalScope.launch(Dispatchers.IO) {
            val insertedReminder = ReminderDTO(
                "test title",
                "test description",
                "test location",
                30.00,
                30.00,
                "testingId"
            )
            reminderDataSource.saveReminder(
                insertedReminder
            )
            launchFragmentInContainer<ReminderListFragment>(Bundle.EMPTY, R.style.AppTheme)

            onView(withId(R.id.noDataTextView)).check(matches(CoreMatchers.not(isDisplayed())))

            onView(withText(insertedReminder.title)).check(matches(isDisplayed()))
            onView(withText(insertedReminder.description)).check(matches(isDisplayed()))
            onView(withText(insertedReminder.location)).check(matches(isDisplayed()))
        }
    }
}
