package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// TODO: Add testing implementation to the RemindersLocalRepository.kt

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var remindersDB: RemindersDatabase
    private lateinit var remindersRepo: RemindersLocalRepository

    @Before
    fun createDbAndRepo() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        remindersDB = Room.inMemoryDatabaseBuilder(
            context,
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersRepo = RemindersLocalRepository(remindersDB.reminderDao())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        remindersDB.close()
    }

    @Test
    fun saveAndGetAllReminders() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedReminder = ReminderDTO(
                "test title",
                "test description",
                "test location",
                30.00,
                30.00
            )
            remindersDB.reminderDao().saveReminder(
                insertedReminder
            )

            val allReminders = remindersRepo.getReminders() as Result.Success
            Assert.assertEquals(allReminders.data[0], insertedReminder)
        }
    }

    @Test
    fun saveAndGetOneReminderById() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedReminder = ReminderDTO(
                "test title",
                "test description",
                "test location",
                30.00,
                30.00,
                "testingId"
            )
            remindersDB.reminderDao().saveReminder(
                insertedReminder
            )

            val allReminders = remindersRepo.getReminder("testingId") as Result.Success
            Assert.assertEquals(allReminders.data, insertedReminder)
        }
    }
}