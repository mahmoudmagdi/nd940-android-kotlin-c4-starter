package com.udacity.project4.locationreminders.savereminder

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    //TODO: provide testing to the SaveReminderView and its live data objects

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var repo: FakeDataSource

    @Before
    fun setupViewModel() {
        GlobalScope.launch(Dispatchers.Main) {
            repo = FakeDataSource()
            viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), repo)
            stopKoin()
            modules()
        }
    }

    private fun modules() {
        val mModule = module {
            single {
                viewModel
            }
        }
        startKoin {
            modules(listOf(mModule))
        }
    }

    @Test
    fun saveReminderTest() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedReminder =
                ReminderDataItem(
                    "test title",
                    "test description",
                    "test location",
                    30.00,
                    30.00,
                    "test_id"
                )
            viewModel.saveReminder(insertedReminder)

            assertEquals(insertedReminder, repo.getReminder("test_id"))
        }
    }

    @Test
    fun setPointOfInterestTest() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedPointOfInterest =
                PointOfInterest(LatLng(30.00, 30.00), "place_id", "location name")

            viewModel.setPointOfInterest(insertedPointOfInterest)

            assertEquals(insertedPointOfInterest, viewModel.selectedPOI)
            assertEquals(insertedPointOfInterest.name, viewModel.reminderSelectedLocationStr)
            assertEquals(insertedPointOfInterest.latLng.latitude, viewModel.latitude)
            assertEquals(insertedPointOfInterest.latLng.longitude, viewModel.longitude)
        }
    }

    @Test
    fun validateEnteredDataTestWithoutTitle() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedReminder =
                ReminderDataItem(
                    "",
                    "test description",
                    "test location",
                    30.00,
                    30.00,
                    "test_id"
                )
            viewModel.validateEnteredData(insertedReminder)
            assertEquals(viewModel.showSnackBarInt, R.string.err_enter_title)
        }
    }

    @Test
    fun validateEnteredDataTestWithoutLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedReminder =
                ReminderDataItem(
                    "test title",
                    "test description",
                    "",
                    30.00,
                    30.00,
                    "test_id"
                )
            viewModel.validateEnteredData(insertedReminder)
            assertEquals(viewModel.showSnackBarInt, R.string.err_enter_title)
        }
    }
}