package com.udacity.project4.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.data.FakeDataSource
import com.udacity.project4.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var remindersRepository: FakeDataSource

    @Before
    fun setupViewModel() {
        stopKoin()

        remindersRepository = FakeDataSource()

        remindersListViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(), remindersRepository
        )
    }

    @Test
    fun loadReminders_loading() {
        // GIVEN - We will load the reminders

        mainCoroutineRule.pause() // pausing the dispatcher
        remindersListViewModel.loadReminders()

        // WHEN - the working thread is Paused we should see the Loader
        assert(remindersListViewModel.showLoading.getOrAwaitValue())

        // THEN - After the dispatcher is resumed we should see the Loader
        mainCoroutineRule.resume()
        assert(!remindersListViewModel.showLoading.getOrAwaitValue())
    }

    @Test
    fun loadReminders_loadingError() {
        // GIVEN - We will load the reminders

        mainCoroutineRule.pause() // pausing the dispatcher
        remindersRepository.setReturnError(true)
        remindersListViewModel.loadReminders()

        // WHEN - the working thread is Paused we should see the Loader
        assert(remindersListViewModel.showLoading.getOrAwaitValue())

        // THEN - After the dispatcher is resumed we should see the Loader
        mainCoroutineRule.resume()
        assert(!remindersListViewModel.showLoading.getOrAwaitValue())
    }

    @Test
    fun loadReminders_noData() {
        // GIVEN - We will load the reminders

        mainCoroutineRule.pause() // pausing the dispatcher
        remindersRepository.setReturnError(true)
        remindersListViewModel.loadReminders()

        // WHEN - the working thread is Paused we should see the Loader
        assert(remindersListViewModel.showLoading.getOrAwaitValue())

        // THEN - After the dispatcher is resumed we should see the Loader
        mainCoroutineRule.resume()
        assert(!remindersListViewModel.showLoading.getOrAwaitValue())
    }
}