package com.udacity.project4.locationreminders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityReminderDescriptionBinding
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

/**
 * Activity that displays the reminder details after the user clicks on the notification
 */
class ReminderDescriptionActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ReminderDataItem = "EXTRA_ReminderDataItem"

        // Receive the reminder object after the user clicks on the notification
        fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {
            val intent = Intent(context, ReminderDescriptionActivity::class.java)
            intent.putExtra(EXTRA_ReminderDataItem, reminderDataItem)
            return intent
        }
    }

    private lateinit var binding: ActivityReminderDescriptionBinding
    private lateinit var geoFencingClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_reminder_description
        )

        // COMPLETED-TODO: Add the implementation of the reminder details
        val reminderItemDescription =
            intent.getSerializableExtra(EXTRA_ReminderDataItem) as ReminderDataItem
        geoFencingClient = LocationServices.getGeofencingClient(this)

        binding.reminderDataItem = reminderItemDescription
        geoFencingClient.removeGeofences(listOf(reminderItemDescription.id)).run {
            addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this@ReminderDescriptionActivity,
                        "reminder item has been deleted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ReminderDescriptionActivity,
                        "there is an issue and the reminder item hasn't been deleted",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
