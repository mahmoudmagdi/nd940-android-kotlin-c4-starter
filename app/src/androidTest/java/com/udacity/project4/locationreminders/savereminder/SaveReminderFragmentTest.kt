package com.udacity.project4.locationreminders.savereminder

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock

//    COMPLETED-TODO: test the navigation of the fragments.
//    COMPLETED-TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SaveReminderFragmentTest {

    private lateinit var reminderDataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    private val _binding = DataBindingIdlingResource()

    @Before
    fun setup() {
        GlobalScope.launch(Dispatchers.Main) {
            reminderDataSource = FakeDataSource()
            saveReminderViewModel =
                SaveReminderViewModel(getApplicationContext(), reminderDataSource)
        }
        stopKoin()
        modules()
    }

    private fun modules() {
        val mModule = module {
            single {
                saveReminderViewModel
            }
        }
        startKoin {
            modules(listOf(mModule))
        }
    }

    @After
    fun close() {
        stopKoin()
    }

    @Test
    fun add_testing_for_the_error_messages() {
        val fragment = launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)
        _binding.monitorFragment(fragment)

        val navController = mock(NavController::class.java)
        fragment.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.saveReminder)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.err_enter_title)))
    }


    @Test
    fun add_testing_for_the_error_messages_for_location() {
        val fragment = launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)
        _binding.monitorFragment(fragment)

        val navController = mock(NavController::class.java)
        fragment.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.reminderTitle)).perform(ViewActions.typeText("test title"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveReminder)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.err_select_location)))
    }
}
