package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.launch

class SaveReminderViewModel(val app: Application, private val dataSource: ReminderDataSource) :
    BaseViewModel(app) {

    private val _isLocationDefined = MutableLiveData<Boolean>()
    val isLocationDefined: LiveData<Boolean>
        get() = _isLocationDefined

    val reminderTitle = MutableLiveData<String>()
    val reminderDescription = MutableLiveData<String>()

    private val _selectedPOI = MutableLiveData<PointOfInterest>()
    val selectedPOI: LiveData<PointOfInterest>
        get() = _selectedPOI

    private val _reminderSelectedLocationStr = MutableLiveData<String>()
    val reminderSelectedLocationStr: LiveData<String>
        get() = _reminderSelectedLocationStr

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get() = _longitude

    init {
        _isLocationDefined.value = false
    }

    /**
     * Clear the live data objects to start fresh next time the view model gets called
     */
    fun onClear() {
        reminderTitle.value = null
        reminderDescription.value = null

        _selectedPOI.value = null
        _reminderSelectedLocationStr.value = null
        _latitude.value = null
        _longitude.value = null
    }

    /**
     * Validate the entered data then saves the reminder data to the DataSource
     */
    fun validateAndSaveReminder(reminderData: ReminderDataItem) {
        if (validateEnteredData(reminderData)) {
            saveReminder(reminderData)
        }
    }

    /**
     * Save the reminder to the data source
     */
    fun saveReminder(reminderData: ReminderDataItem) {
        showLoading.value = true
        viewModelScope.launch {
            dataSource.saveReminder(
                ReminderDTO(
                    reminderData.title,
                    reminderData.description,
                    reminderData.location,
                    reminderData.latitude,
                    reminderData.longitude,
                    reminderData.id
                )
            )

            showLoading.value = false
            showToast.value = app.getString(R.string.reminder_saved)
            navigationCommand.value = NavigationCommand.Back
        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    fun validateEnteredData(reminderData: ReminderDataItem): Boolean {
        if (reminderData.title.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_enter_title
            return false
        }

        if (reminderData.location.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_select_location
            return false
        }
        return true
    }

    fun setPointOfInterest(selectedPoi: PointOfInterest) {
        Log.e("selectedPoi.latLng.latitude", selectedPoi.latLng.latitude.toString())
        Log.e("selectedPoi.latLng.longitude", selectedPoi.latLng.longitude.toString())

        _selectedPOI.value = selectedPoi
        _latitude.value = selectedPoi.latLng.latitude
        _longitude.value = selectedPoi.latLng.longitude
        _reminderSelectedLocationStr.value = selectedPoi.name
    }

    fun onLocationSelected() {
        _isLocationDefined.value = true
    }
}