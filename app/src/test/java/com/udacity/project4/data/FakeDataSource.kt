package com.udacity.project4.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource : ReminderDataSource {

    var dataSource: HashMap<String, ReminderDTO> = LinkedHashMap()
    var loadData: Boolean = true

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            if (loadData) {
                val tmpList: MutableList<ReminderDTO> = ArrayList()
                dataSource.forEach {
                    tmpList.add(it.value)
                }
                Result.Success(tmpList)
            } else {
                throw Exception("Fail To Load Data")
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        dataSource[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return try {
            if (loadData) {
                dataSource[id]?.let {
                    Result.Success(it)
                }
                return Result.Error("item not found")
            } else {
                throw Exception("Fail To Load Data")
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        dataSource.clear()
    }

    fun setReturnError(b: Boolean) {
        loadData = b
    }
}