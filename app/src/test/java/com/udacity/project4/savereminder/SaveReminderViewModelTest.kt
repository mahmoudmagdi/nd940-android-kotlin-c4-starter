package com.udacity.project4.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersLocalRepository: ReminderDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @Before
    fun initTheRpoAndViewMode() {
        stopKoin()

        remindersLocalRepository = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            remindersLocalRepository
        )
    }

    @Test
    fun validateReminder_Invalid() {
        // GIVEN - invalid reminder
        val reminder = ReminderDataItem(
            title = null,
            description = "Desc",
            location = "loc",
            latitude = 1.2,
            longitude = 1.3,
            id = "someid"
        )

        // WHEN - validate reminder
        val result = saveReminderViewModel.validateEnteredData(reminder)

        // THEN - reminder is invalid
        assertThat(result, `is`(false))
    }

    @Test
    fun validateReminder_Valid() {
        // GIVEN - valid reminder
        val reminder = ReminderDataItem(
            title = "title",
            description = "Desc",
            location = "loc",
            latitude = 1.2,
            longitude = 1.3,
            id = "someid"
        )

        // WHEN - validate reminder
        val result = saveReminderViewModel.validateEnteredData(reminder)

        // THEN - reminder is valid
        assertThat(result, `is`(true))
    }
}