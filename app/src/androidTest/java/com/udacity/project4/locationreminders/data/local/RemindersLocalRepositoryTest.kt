package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// TODO: Add testing implementation to the RemindersLocalRepository.kt

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var remindersDB: RemindersDatabase
    private lateinit var remindersDao: RemindersDao
    private lateinit var remindersRepo: RemindersLocalRepository

    @Before
    fun setup() {
        remindersDB = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        remindersDao = remindersDB.reminderDao()
        remindersRepo = RemindersLocalRepository(remindersDao)
    }

    @After
    fun closeDb() {
        remindersDB.close()
    }

    @Test
    fun saveReminder_retrieveReminder() = runBlocking {

        // GIVEN - A new reminder saved in the database.
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 0.0,
            longitude = 0.0
        )
        remindersRepo.saveReminder(reminder)
        val result = remindersRepo.getReminder(reminder.id)
        Assert.assertEquals(result is Result.Success, true)
        result as Result.Success
        Assert.assertEquals(result.data.title, reminder.title)
        Assert.assertEquals(result.data.description, reminder.description)
        Assert.assertEquals(result.data.location, reminder.location)
        Assert.assertEquals(result.data.latitude, reminder.latitude)
        Assert.assertEquals(result.data.longitude, reminder.longitude)
    }

    @Test
    fun deleteAllReminders_getReminders() = runBlocking {
        // GIVEN - A new reminder saved in the database.
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 0.0,
            longitude = 0.0
        )
        remindersRepo.saveReminder(reminder)
        remindersRepo.deleteAllReminders()
        val result = remindersRepo.getReminders()
        Assert.assertEquals(result is Result.Success, true)
        result as Result.Success
        Assert.assertEquals(result.data.isEmpty(), true)
    }

    @Test
    fun getReminderById() = runBlocking {
        // GIVEN - A new reminder saved in the database.
        val reminder = ReminderDTO(
            title = "title",
            description = "description",
            location = "location",
            latitude = 0.0,
            longitude = 0.0
        )
        remindersRepo.saveReminder(reminder)
        val result = remindersRepo.getReminder(reminder.id)
        Assert.assertEquals(result is Result.Success, true)
        result as Result.Success
        Assert.assertEquals(result.data.title, reminder.title)
        Assert.assertEquals(result.data.description, reminder.description)
        Assert.assertEquals(result.data.location, reminder.location)
        Assert.assertEquals(result.data.latitude, reminder.latitude)
        Assert.assertEquals(result.data.longitude, reminder.longitude)
    }
}