package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// TODO: Add testing implementation to the RemindersDao.kt

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private lateinit var remindersDB: RemindersDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        remindersDB = Room.inMemoryDatabaseBuilder(
            context,
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        remindersDB.close()
    }

    @Test
    fun insertAndGetAllReminders() {
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

            val allReminders = remindersDB.reminderDao().getReminders()
            assertEquals(allReminders[0], insertedReminder)
        }
    }

    @Test
    fun insertAndGetOneReminderById() {
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

            val selectedReminder = remindersDB.reminderDao().getReminderById("testingId")
            assertEquals(selectedReminder, insertedReminder)
        }
    }

    @Test
    fun deleteAllReminders() {
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
            remindersDB.reminderDao().saveReminder(
                insertedReminder
            )

            remindersDB.reminderDao().deleteAllReminders()
            val allReminders = remindersDB.reminderDao().getReminders()
            assertEquals(allReminders.isEmpty(), true)
        }
    }
}