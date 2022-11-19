package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO: Add testing implementation to the RemindersDao.kt

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var remindersDB: RemindersDatabase

    @Before
    fun initDb() {
        remindersDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        remindersDB.close()
    }

    @Test
    fun saveReminderAndGetById() = runBlockingTest {
        val reminder = ReminderDTO(
            title = "title",
            description = "some randomTxt",
            location = "loc",
            latitude = 1.1,
            longitude = 2.2,
            id = "id1"
        )
        remindersDB.reminderDao().saveReminder(reminder)
        val loaded = remindersDB.reminderDao().getReminderById(reminder.id)
        assertEquals(loaded as ReminderDTO, reminder)
    }

    @Test
    fun saveReminderAndDelete() = runBlockingTest {
        val reminder = ReminderDTO(
            title = "title",
            description = "some randomTxt",
            location = "loc",
            latitude = 1.1,
            longitude = 2.2,
            id = "id1"
        )
        remindersDB.reminderDao().saveReminder(reminder)
        remindersDB.reminderDao().deleteAllReminders()
        val loaded = remindersDB.reminderDao().getReminderById(reminder.id)
        assertEquals(loaded, null)
    }

    @Test
    fun saveReminderAndGetReminders() = runBlockingTest {
        val reminder = ReminderDTO(
            title = "title",
            description = "some randomTxt",
            location = "loc",
            latitude = 1.1,
            longitude = 2.2,
            id = "id1"
        )
        remindersDB.reminderDao().saveReminder(reminder)
        val loaded = remindersDB.reminderDao().getReminders()
        assertEquals(loaded[0], reminder)
    }
}