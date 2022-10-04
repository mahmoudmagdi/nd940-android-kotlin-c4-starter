package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

// Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    private val remindersList = ArrayList<ReminderDTO>()
    // TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        // TODO("Return the reminders")
        return Result.Success(remindersList)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        // TODO("save the reminder")
        remindersList.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        // TODO("return the reminder with the id")
        remindersList.forEach {
            if (it.id == id) {
                return Result.Success(it)
            }
        }

        return Result.Error("there is no reminder item with ID: $id")
    }

    override suspend fun deleteAllReminders() {
        // TODO("delete all the reminders")
        remindersList.clear()
    }
}